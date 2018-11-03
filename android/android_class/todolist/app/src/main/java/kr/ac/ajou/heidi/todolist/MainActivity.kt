package kr.ac.ajou.heidi.todolist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun createBody(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("email", emailEditText.text.toString())
        jsonObject.addProperty("password", passwordEditText.text.toString())
        return jsonObject
    }

    private fun signIn() {
        val call = mongoApi.getAccessToken(createBody())
        call.enqueue(object : Callback<Auth> {
            override fun onFailure(call: Call<Auth>, t: Throwable) {
                Log.e(TAG, t.localizedMessage)
            }

            override fun onResponse(call: Call<Auth>, response: Response<Auth>) {
                val result = response.body()
                result?.let {
                        result ->
                    updateToken(this@MainActivity, result.accessToken)
                    startActivity<ImageActivity>()
                }
            }
        })
    }
}
