package com.example.user.chatfirebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class SignInActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 123
        // 숫자는 상관없고, 어떤 액티비티에서 온 결과값인지 상수로 나타낸다.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val providers: List<AuthUI.IdpConfig> = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
        )


        googleSignInButton.setOnClickListener {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), RC_SIGN_IN)

        }

    }

    //resultCode: 성공 실패
    //requestcode : 어떤 액티비티
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser

                user?.let {
                    //Log.i("SignInActivity", "user: ${user.uid}")
                    //anko가 지원하는 기능.
                    startActivity<ChatActivity>()
                }
            } else {
                Log.e("SignInActivity", "Sign In Failed")
            }
        }
    }
}

//reified: 제네릭 인자의 정보가 소거되지 않게 해준다.
inline fun<reified E> Context.startActivity2() {
    val intent = Intent(this, E::class.java)
    startActivity(intent)
}