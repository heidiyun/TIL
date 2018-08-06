package com.example.user.chatfirebase.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.user.chatfirebase.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

// Firebase의 서비스 계정 파일은 절대 공개된 Github에 올리면 안된다.
//  => app/.gitignore에 google-services.json을 추가한다.
// 1) Tools -> Firebase -> Authentication
// Connect your app to Firebase (Firebase 계정 로그인 확인 & Firebase 프로젝트와 연결)
//  => app/google-services.json을 생성.

// Add Firebase Authentication
//  => build.gradle 에 의존성 추가.

// Optional: Configure ProGuard (난독화 시, 유지해야 할 것 추가.)
// proguard=rules.pro 에
// -keepattributes Signature
// -keepattributes *Annotation* 추가.

// 2) Firebase 문서로 이동 -> 가이드 -> Android -> 기본 UI로 로그인




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

    // 로그인 흐름이 완료되면 결과를 수신하는 메소드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                // 접속한 유저의 정보를 받아온다.

                user?.let {
                    //Log.i("SignInActivity", "user: ${user.uid}")
                    //anko가 지원하는 기능.
                    startActivity<ChatActivity>()
                    // 유저 정보를 받아왔으면 다음 액티비티를 실행한다.
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

