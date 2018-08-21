package com.example.user.fragmentexample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_signin.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.toast
import java.io.IOException
//
//data class Auth(
//        @field:SerializedName("access_token")
//        val accessToken: String,
//        @field:SerializedName("token_type")
//        val tokenType: String)

class SignInActivity : AppCompatActivity() {

    companion object {
        // const val TAG = "SignInActivity"
        val TAG = SignInActivity::class.java.simpleName

        const val CLIENT_ID = "d3be1425f2b68349c8cf"
        const val CLIENT_SECRET = "8f604dacb00a15456bd8854fe003874d33287c30"
    }


    // Intent?
    //  : 의도
    //  tel://010-1234-5678                 : 전화
    //  sms://010-1234-5678?message=안녕하세요 : 문자
    //  https://github.com/login/oauth/authorize?client_id=XXX : 브라우져

    // apply, let: 수신 객체 지정 람다
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        button.setOnClickListener {
            // https://github.com/login/oauth/authorize?client_id=XXX
            val authUri = Uri.Builder().scheme("https")
                    .authority("github.com")
                    .appendPath("login")
                    .appendPath("oauth")
                    .appendPath("authorize")
                    .appendQueryParameter("client_id", CLIENT_ID)
                    .build()

            // toast(authUri.toString())

            // 인터넷 브라우저를 실행하는 Intent
            // => Custom Tabs: android-support-library
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(this, authUri)
        }
    }

    // 기존의 액티비티가 다시 사용될 경우...
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        toast("onNewIntent")
        check(intent != null)
        check(intent?.data != null)

        val uri = intent?.data
        val code = uri?.getQueryParameter("code") ?: throw IllegalStateException("no code!!")

        getAccessToken(code)
    }

    private fun post(url: String, clientId: String, clientSecret: String, code: String) {
        // val body = Body(clientId, clientSecret, code)

        val map = mapOf(
                "client_id" to clientId,
                "client_secret" to clientSecret,
                "code" to code
        )

        val gson = GsonBuilder().create()
        val json = gson.toJson(map)

        val requestBody = RequestBody
                .create(MediaType.parse("application/json; charset=utf-8"), json)
        val request = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .post(requestBody).build()

//        val response = httpClient.newCall(request).execute()
//        Log.i("Request", response.body().toString())

        // 비동기로 처리해야 한다.
        val call = httpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    toast("Failed")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    toast("Succeed")

                    val statusCode = response.code()
                    when (statusCode) {
                        in 200.until(300) -> {
                            // toast("Status OK")
                            response.body()?.let {
                                val result = it.string()

                                val auth = gson.fromJson(result, Auth::class.java)
                                Log.i(TAG, auth.toString())
                            }
                        }
                        in 400.until(500) -> toast("Client Error")
                        in 500.until(600) -> toast("Server Error")
                    }
                }
            }
        })

    }


    private fun getAccessToken(code: String) {
        Log.i(TAG, "getAccessToken: $code")

        post("https://github.com/login/oauth/access_token",
                CLIENT_ID, CLIENT_SECRET, code)

        // HTTP Request - POST
        // https://github.com/login/oauth/access_token
        // {
        //     "client_id": <client_id>,
        //     "client_secret": <client_secret>,
        //     "code": <code>
        // }
    }
}

