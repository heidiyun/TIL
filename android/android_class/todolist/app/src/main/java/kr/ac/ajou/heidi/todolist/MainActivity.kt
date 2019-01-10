package kr.ac.ajou.heidi.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
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

        signUpButton.setOnClickListener {
            val fragment = SignUpFragment()

            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .addToBackStack(null).commit()

            signInButton.visibility = View.GONE
            signUpButton.visibility = View.GONE

        }
    }

    private fun createBody(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("email", emailEditText.text.toString())
        jsonObject.addProperty("password", passwordEditText.text.toString())
        return jsonObject
    }

    override fun onBackPressed() {
        if (signInButton.visibility == View.GONE) {
            signInButton.visibility = View.VISIBLE
            signUpButton.visibility = View.VISIBLE

        } else
        super.onBackPressed()

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
                    Log.i("MainActivity", it.accessToken)
                    updateToken(this@MainActivity, it.accessToken)
                    startActivity<TodoListActivity>()
                }
            }
        })
    }
}
