package com.example.user.kakaologin

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import com.kakao.auth.*
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.security.MessageDigest

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().checkAndImplicitOpen()

//        getKeyHash()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data))
            return
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun requestMe() {
        val keys = listOf(
                "properties.nickname",
                "properties.profile_image"
        )

        UserManagement.getInstance().me(keys, object : MeV2ResponseCallback() {
            override fun onSuccess(result: MeV2Response?) {
                result?.let {
                    Log.i("Oauth_app", "ID: ${result.id}")
                    Log.i("Oauth_app", "ID: ${result.nickname}")
                    Log.i("Oauth_app", "ID: ${result.profileImagePath}")

                    textView.text = result.nickname

                }


            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
            }

        })
    }

    private fun getKeyHash() {
        try {
            val info: PackageInfo = packageManager.getPackageInfo("com.example.user.signinactivity", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("SignInActivity", "key_hash=" + Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    inner class SessionCallback : ISessionCallback {
        override fun onSessionOpenFailed(exception: KakaoException?) {
            exception?.let {
                it.printStackTrace()
            }
        }

        override fun onSessionOpened() {
            Log.i("Oauth_app", "onSessionOpened")
            requestMe()
        }

    }
}



class GlobalApplication : Application() {
    class KakaoSDKAdapter(val context: Context) : KakaoAdapter() {

        override fun getApplicationConfig(): IApplicationConfig {
            return IApplicationConfig {
                context
            }
        }

        override fun getSessionConfig(): ISessionConfig {
            return object : ISessionConfig {
                override fun isSaveFormData(): Boolean {
                    return true
                }

                override fun getAuthTypes(): Array<AuthType> {
                    return arrayOf(AuthType.KAKAO_LOGIN_ALL)
                }

                override fun isSecureMode(): Boolean {
                    return false
                }

                override fun getApprovalType(): ApprovalType {
                    return ApprovalType.INDIVIDUAL
                }

                override fun isUsingWebviewTimer(): Boolean {
                    return false
                }

            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSDK.init(KakaoSDKAdapter(this))
    }
}