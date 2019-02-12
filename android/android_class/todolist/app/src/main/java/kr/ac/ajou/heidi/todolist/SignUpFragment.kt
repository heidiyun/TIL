package kr.ac.ajou.heidi.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_sign_up, container, false)

        view.signUpButton.setOnClickListener {
            signUp()
        }

        return view
    }

    private fun signUp() {
        mongoApi.registerUser(createBody()).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                fragmentManager?.beginTransaction()?.remove(this@SignUpFragment)?.commit()
            }
        })
    }


    private fun createBody(): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", nameEditText.text.toString())
        jsonObject.addProperty("email", emailEditText.text.toString())
        jsonObject.addProperty("password", passwordEditText.text.toString())
        return jsonObject
    }
}