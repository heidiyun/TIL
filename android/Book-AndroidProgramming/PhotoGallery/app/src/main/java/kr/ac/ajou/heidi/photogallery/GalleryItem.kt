package kr.ac.ajou.heidi.photogallery

import android.net.Uri

class GalleryItem {
    var caption: String = ""
    var id: String = ""
    var url: String = ""
    var owner: String = ""

    override fun toString(): String {
        return caption
    }

    fun getPhotoPageUri() : Uri {
        return Uri.parse("http://www.flickr.com/photos/")
            .buildUpon()
            .appendPath(owner)
            .appendPath(id)
            .build()
    }
}