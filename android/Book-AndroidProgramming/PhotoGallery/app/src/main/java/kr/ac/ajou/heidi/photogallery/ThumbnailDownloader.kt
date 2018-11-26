package kr.ac.ajou.heidi.photogallery

import android.os.HandlerThread
import android.util.Log

class ThumbnailDownloader<T> : HandlerThread(TAG) {
    companion object {
        val TAG = "ThumbnailDownloader"
    }

    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "Got a URL: $url")
    }
}