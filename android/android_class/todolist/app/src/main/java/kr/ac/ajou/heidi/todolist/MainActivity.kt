package kr.ac.ajou.heidi.todolist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jsonObject = JsonObject()
        jsonObject.addProperty("_id", "5bca06b12b7feb112ba59a1e")
//        jsonObject.addProperty("name", "heidiyun")
//        jsonObject.addProperty("email", "heidiyun@naver.com")

        button.setOnClickListener {view ->

            val call = mongoApi.getUserInfo()
            call.enqueue(object : Callback<Array<CreatedUserInfo>> {
                override fun onFailure(call: Call<Array<CreatedUserInfo>>, t: Throwable) {
                    Log.i("MainActivity", t.message)
                }

                override fun onResponse(call: Call<Array<CreatedUserInfo>>, response: Response<Array<CreatedUserInfo>>) {
                    val result = response.body()
                    result?.let {
                        Log.i("MainActivity", "hihih")
//                        runOnUiThread {
//                            nameText.text = it.name
//                            emailText.text = it.email
//                        }
                    }
                }

            })

        }

    }
}
