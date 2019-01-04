package kr.ac.ajou.heidi.photogallery

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_photo_gallery.view.*
import kotlinx.android.synthetic.main.gallery_item.view.*

class PhotoGalleryFragment : VisibleFragment() {

    inner class FetchItemsTask(val query: String?) : AsyncTask<Void, Void, ArrayList<GalleryItem>>() {

        override fun doInBackground(vararg params: Void?): ArrayList<GalleryItem> {

            return if (query == null) FlickrFetchr().fetchRecentPhotos()
            else FlickrFetchr().searchPhotos(query)
        }

        override fun onPostExecute(result: ArrayList<GalleryItem>?) {
            items = result ?: ArrayList()
            adapter.galleryItems = items
            adapter.notifyDataSetChanged()
        }
    }

    inner class PhotoHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.gallery_item, parent, false)
            )

    inner class PhotoAdapter : RecyclerView.Adapter<PhotoHolder>() {

        var galleryItems = emptyList<GalleryItem>()
        var drawable: Drawable? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            return PhotoHolder(parent)
        }

        override fun getItemCount(): Int = galleryItems.size

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            with(holder.itemView) {
                val placeholder = ContextCompat.getDrawable(context, R.drawable.bill_up_close)
                fragmentPhotoGalleryImageView.setImageDrawable(placeholder)
                thumbnailDownloader?.queueThumbnail(holder, galleryItem.url)
                fragmentPhotoGalleryImageView.setImageDrawable(drawable)
                this.setOnClickListener {
                    activity?.applicationContext?.let { context ->
                        val intent = PhotoPageActivity.newIntent(context, galleryItem.getPhotoPageUri())
                        startActivity(intent)
                    }


                }
            }
        }
    }


    companion object {
        val TAG = PhotoGalleryFragment::class.java.name.toString()
        fun newInstance(): PhotoGalleryFragment {
            return PhotoGalleryFragment()
        }
    }

    var items = ArrayList<GalleryItem>()
    val adapter = PhotoAdapter()
    var thumbnailDownloader: ThumbnailDownloader<PhotoHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
        updateItems()

        val responseHandler = Handler()
        thumbnailDownloader = ThumbnailDownloader(responseHandler)
        thumbnailDownloader?.setThumbnailDownloaderListener(
                object : ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder> {
                    override fun onThumbnailDownloaded(target: PhotoHolder, thumbnail: Bitmap) {
                        val drawable = BitmapDrawable(resources, thumbnail)
                        adapter.drawable = drawable
                    }
                }
        )

        thumbnailDownloader?.start()
        thumbnailDownloader?.looper
        Log.i(TAG, "Background thread started")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

//            val intent = PollService.newIntent(view.context)
//            activity?.startService(intent)
//        PollService.setServiceAlarm(view.context, true)

        view.fragmentPhotoGalleryRecyclerView.layoutManager = GridLayoutManager(view.context, 3)
        adapter.galleryItems = items
        if (isAdded) {
            view.fragmentPhotoGalleryRecyclerView.adapter = adapter
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_photo_gallery, menu)

        val searchItem = menu?.findItem(R.id.menu_item_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                QueryPreferences.setStoredQuery(view?.context ?: return false, query ?: "")
                updateItems()
                searchView.isIconified = true
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        searchView.setOnSearchClickListener(View.OnClickListener {
            val query = QueryPreferences.getStoredQuery(view?.context ?: return@OnClickListener)
            searchView.setQuery(query, false)
        })

        val toggleItem = menu.findItem(R.id.menu_item_toggle_polling)
        if (PollService.isServiceAlarmOn(activity ?: return)) {
            toggleItem.setTitle(R.string.stop_polling)
        } else {
            toggleItem.setTitle(R.string.start_polling)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_item_clear -> {
                QueryPreferences.setStoredQuery(view?.context ?: return false, null)
                updateItems()
                return true
            }
            R.id.menu_item_toggle_polling -> {
                view?.context?.let {
                    Log.i(TAG, "menu view")
                    val shouldStartAlarm = !PollService.isServiceAlarmOn(it)
                    PollService.setServiceAlarm(it, shouldStartAlarm)
                    activity?.invalidateOptionsMenu()
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateItems() {
        val query = QueryPreferences.getStoredQuery(view?.context ?: return)
        FetchItemsTask(query).execute()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        thumbnailDownloader?.clearQueue()
    }

    override fun onDestroy() {
        super.onDestroy()
        thumbnailDownloader?.quit()
        Log.i(TAG, "Background thread destroyed")
    }


}