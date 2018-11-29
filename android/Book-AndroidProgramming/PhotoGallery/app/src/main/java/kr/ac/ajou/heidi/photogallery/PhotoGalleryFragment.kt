package kr.ac.ajou.heidi.photogallery

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_photo_gallery.view.*
import kotlinx.android.synthetic.main.gallery_item.view.*

class PhotoGalleryFragment : Fragment() {

    inner class FetchItemsTask : AsyncTask<Void, Void, ArrayList<GalleryItem>>() {

        override fun doInBackground(vararg params: Void?): ArrayList<GalleryItem> {
            return FlickrFetchr().fetchItems()
        }

        override fun onPostExecute(result: ArrayList<GalleryItem>?) {
            items = result ?: ArrayList()
            adapter.galleryItems = items
            adapter.notifyDataSetChanged()
        }

        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }

    }

    inner class PhotoHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.gallery_item, parent, false))

    inner class PhotoAdapter : RecyclerView.Adapter<PhotoHolder>() {

        var galleryItems = emptyList<GalleryItem>()
        var drawable: Drawable? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder = PhotoHolder(parent)

        override fun getItemCount(): Int = galleryItems.size

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            with(holder.itemView) {
                val placeholder = ContextCompat.getDrawable(context, R.drawable.bill_up_close)
                fragmentPhotoGalleryImageView.setImageDrawable(placeholder)
                thumbnailDownloader?.queueThumbnail(holder, galleryItem.url)
                fragmentPhotoGalleryImageView.setImageDrawable(drawable)
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
    var thumbnailDownloader : ThumbnailDownloader<PhotoHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        FetchItemsTask().execute()

        val responseHandler = Handler()
        thumbnailDownloader = ThumbnailDownloader(responseHandler)
        thumbnailDownloader?.setThumbnailDownloaderListener(
            object: ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder> {
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

        view.fragmentPhotoGalleryRecyclerView.layoutManager = GridLayoutManager(view.context, 3)
        adapter.galleryItems = items
        if (isAdded) {
            view.fragmentPhotoGalleryRecyclerView.adapter = adapter
        }

        return view
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