package kr.ac.ajou.heidi.todolist

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_image.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class ImageActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ImageActivity"
        const val SELECT_PICTURE = 1
        fun copyStream(input: InputStream, output: OutputStream) {
            val buffer = ByteArray(1024)
            var bytesRead: Int = 0
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead)
                bytesRead = input.read(buffer)
            }
        }
    }


    private var signedUrl: String? = null
    private var photoFile: File? = null
    private var requestBody: RequestBody? = null
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        uploadButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {

            selectedImageUri = data?.data ?: return
            if (requestCode == SELECT_PICTURE) {
                try {
                    Log.i(TAG, "hing")
                    photoFile = createImageFile()

                    val inputStream = contentResolver.openInputStream(selectedImageUri)
                    val fileOutputStream = FileOutputStream(photoFile)

                    copyStream(inputStream, fileOutputStream)
                    fileOutputStream.close()
                    inputStream.close()
                } catch (e: Exception) {
                    Log.i(TAG, e.localizedMessage)
                }


                if (checkPermission() == PackageManager.PERMISSION_GRANTED) {
                    getSignedUrl(this)
                }
            }
        }
    }

    private fun getSignedUrl(context: Context) {
        val token = getToken(context)
        Log.i(TAG, "${token}")
        val call = mongoApi.getSignedUrl("bearer $token")
        call.enqueue(object : Callback<SignedUrl> {

            override fun onFailure(call: Call<SignedUrl>, t: Throwable) {
                Log.e(TAG, t.localizedMessage)
            }

            override fun onResponse(call: Call<SignedUrl>, response: Response<SignedUrl>) {
                val result = response.body()
                result?.let {
                    signedUrl = it.signedUrl
                    postPhotoFileToStorage(signedUrl!!)

                }
            }
        })

    }

    private fun postPhotoFileToStorage(signedUrl: String) {
        Log.i(TAG, signedUrl)
        val requestBody = createReqeustBody()
        val call = mongoApi.postFile(signedUrl, requestBody)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(ImageActivity.TAG, t.localizedMessage)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {

            }
        })
    }


    private fun createReqeustBody(): RequestBody {
        selectedImageUri?.let { uri ->
            val contentType = contentResolver.getType(uri)
            contentType?.let { type ->
                photoFile?.let { file ->
                     requestBody = RequestBody.create(MediaType.parse(type), file)
                    Log.i(TAG, "requestBody -> $requestBody")
                }
            }
        }

        return requestBody!!

    }

    private fun checkPermission(): Int {
        val permissionCheck =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }

        return permissionCheck

    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat.getDateTimeInstance().format(Date())
        val imageFileName = "image_" + timeStamp + "_"
        val image = File.createTempFile(
            imageFileName,
            ""
        )

        return image
    }

}