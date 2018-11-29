package kr.ac.ajou.heidi.photogallery

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

class ThumbnailDownloader<T>(val responseHandler: Handler) : HandlerThread(TAG) {
    companion object {
        val TAG = "ThumbnailDownloader"
        val MESSAGE_DOWNLOAD = 0
    }

    private var requestHandler: Handler? = null
    val requestMap = ConcurrentHashMap<T, String>()
    var thumbnailDownloadListener: ThumbnailDownloadListener<T>? = null

    interface ThumbnailDownloadListener<T> {
        fun onThumbnailDownloaded(target: T, thumbnail: Bitmap)
    }

    fun setThumbnailDownloaderListener(listener: ThumbnailDownloadListener<T>) {
        thumbnailDownloadListener = listener
    }


    override fun onLooperPrepared() {
        requestHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message?) {
                if (msg?.what == MESSAGE_DOWNLOAD) {
                    val target = msg.obj as T
                    Log.i(TAG, "Got a request for URL: ${requestMap[target]}")
                    handleRequest(target)
                }
            }
        }
    }

    private fun handleRequest(target: T) {
        try {
            val url = requestMap[target] ?: return
            val bitmapBytes = FlickrFetchr().getUrlBytes(url)
            val bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.size)
            Log.i(TAG, "Bitmap created")

            responseHandler.post {
                if (requestMap[target] != url) return@post
                requestMap.remove(target)
                thumbnailDownloadListener?.onThumbnailDownloaded(target, bitmap)
            }

        } catch (e: IOException) {
            Log.e(TAG, "Error downloading image", e)
        }
    }

    fun clearQueue() {
        requestHandler?.removeMessages(MESSAGE_DOWNLOAD)
    }

    fun queueThumbnail(target: T, url: String?) {
        Log.i(TAG, "Got a URL: $url")

        if (url == null) {
            requestMap.remove(target)
        } else {
            requestMap[target] = url
            requestHandler?.obtainMessage(MESSAGE_DOWNLOAD, target)?.sendToTarget()

        }
    }
}