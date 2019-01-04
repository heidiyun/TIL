package kr.ac.ajou.heidi.photogallery

import android.net.Uri
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class FlickrFetchr {
    companion object {
        val TAG = FlickrFetchr::class.java.name.toString()
        const val API_KEY = "63f4169b8ce5d7a34d72ac1ba898448a"
        const val FETCH_RECENTS_METHOD = "flickr.photos.getRecent"
        const val SEARCH_METHOD = "flickr.photos.search"
        val ENDPOINT = Uri.parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .build()

    }

    @Throws(IOException::class)
    fun getUrlBytes(urlSpec: String): ByteArray {
        val url = URL(urlSpec)
        val connection = url.openConnection() as HttpURLConnection

        try {
            val bos = ByteArrayOutputStream()
            val inputStream = connection.inputStream

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException("${connection.responseMessage}:with $urlSpec")
            }

            val buffer = ByteArray(1024)
            var bytesRead = inputStream.read(buffer)
            while (bytesRead > 0) {
                bos.write(buffer, 0, bytesRead)
                bytesRead = inputStream.read(buffer)
                Log.i("flicker", "bytesRad: $bytesRead")
            }
            bos.close()
            return bos.toByteArray()

        } finally {
            connection.disconnect()
        }

    }

    @Throws(IOException::class)
    fun getUrlString(urlSpec: String): String {
        return String(getUrlBytes(urlSpec))
    }

    fun downloadGalleryItems(url: String): ArrayList<GalleryItem> {

        val items = arrayListOf<GalleryItem>()

        try {
            val jsonString = getUrlString(url)
            Log.i(TAG, "Received Json: $jsonString")
            val jsonBody = JSONObject(jsonString)
            parseItems(items, jsonBody)
        } catch (e: JSONException) {
            Log.e(TAG, "Failed to parse JSON $e")
        } catch (e: IOException) {
            Log.e(TAG, "Failed to fetch items  $e")
        }

        return items
    }

    @Throws(IOException::class, JSONException::class)
    fun parseItems(items: ArrayList<GalleryItem>, jsonBody: JSONObject) {
        val photosJsonObject = jsonBody.getJSONObject("photos")
        val photoJsonArray = photosJsonObject.getJSONArray("photo")

        for (i in 0.until(photoJsonArray.length())) {
            val photoJsonObject = photoJsonArray.getJSONObject(i)

            val item = GalleryItem()
            item.id = photoJsonObject.getString("id")
            item.caption = photoJsonObject.getString("title")

            if (!photoJsonObject.has("url_s")) {
                continue
            }

            item.url = photoJsonObject.getString("url_s")
            item.owner = photoJsonObject.getString("owner")
            items.add(item)
        }
    }

    private fun buildUrl(method: String, query: String): String {
        val uriBuilder = ENDPOINT.buildUpon().appendQueryParameter("method", method)

        if (method.equals(SEARCH_METHOD)) {
            uriBuilder.appendQueryParameter("text", query)
        }
        return uriBuilder.build().toString()
    }

    fun fetchRecentPhotos(): ArrayList<GalleryItem> {
        val url = buildUrl(FETCH_RECENTS_METHOD, "")
        return downloadGalleryItems(url)
    }

    fun searchPhotos(query: String): ArrayList<GalleryItem> {
        val url = buildUrl(SEARCH_METHOD, query)
        return downloadGalleryItems(url)
    }
}
