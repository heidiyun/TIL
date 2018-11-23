package kr.ac.ajou.heidi.photogallery

class GalleryItem {
    var caption: String = ""
    var id: String = ""
    var url: String = ""

    override fun toString(): String {
        return caption
    }
}