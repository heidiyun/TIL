package kr.ac.ajou.heidi.photogallery

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_photo_gallery.view.*
import kotlinx.android.synthetic.main.list_item_photo_gallery.view.*

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
                    .inflate(R.layout.list_item_photo_gallery, parent, false))

    inner class PhotoAdapter : RecyclerView.Adapter<PhotoHolder>() {

        var galleryItems = emptyList<GalleryItem>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder = PhotoHolder(parent)

        override fun getItemCount(): Int = galleryItems.size

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            with(holder.itemView) {
                photoTextView.text = galleryItem.toString()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        FetchItemsTask().execute()
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

}