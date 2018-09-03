package com.example.user.kakaologin

import android.annotation.SuppressLint
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
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class SignInActivity : AppCompatActivity() {

    companion object {
        const val OAUTH_CLIENT_ID = "CJAOs2c8Ic9yafFnKx_U"
        const val OAUTH_CLIENT_SECRET = "LAaazdadN5"
        const val OAUTH_CLIENT_NAME = "Login"
    }

    val mOAuthLoginModule = OAuthLogin.getInstance()
    val mOAuthLoginHandler: OAuthLoginHandler = @SuppressLint("HandlerLeak")
    object: OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                val accessToken = mOAuthLoginModule.getAccessToken(this@SignInActivity)
                val refreshToken = mOAuthLoginModule.getRefreshToken(this@SignInActivity)
                val expiresAt: Long = mOAuthLoginModule.getExpiresAt(this@SignInActivity)
                val tokenType = mOAuthLoginModule.getTokenType(this@SignInActivity)
                Log.i("SignInActivity", "accessToken: $accessToken")
                updateToken(this@SignInActivity, accessToken)
            } else {
                val errorCode = mOAuthLoginModule.getLastErrorCode(this@SignInActivity).code
                val errorDesc = mOAuthLoginModule.getLastErrorDesc(this@SignInActivity)
                toast("errorCode: $errorCode, errorDesc: $errorDesc")
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        val callback: SessionCallback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().checkAndImplicitOpen()

        mOAuthLoginModule.init(
                this,
                OAUTH_CLIENT_ID,
                OAUTH_CLIENT_SECRET,
                OAUTH_CLIENT_NAME
        )

        naverLoginButton.setOAuthLoginHandler(mOAuthLoginHandler)
        naverLoginButton.setOnClickListener { mOAuthLoginModule.startOauthLoginActivity(
                this@SignInActivity, mOAuthLoginHandler)
            val userApi = provideUserApi(this)
            val call = userApi.getUserInfo()
            call.enqueue({
                response ->
                val result = response.body()
                result?.let {
                    naverUserName.text = it.response.name
                    Log.e("SignInActivity", "name: ${it.response.name}")
                }
            }, {
                t ->
                Log.e("SignInActivity", "exception: ${t.printStackTrace()}")

            })

        }




    }

    fun <T> Call<T>.enqueue(success : (response: retrofit2.Response<T>) -> Unit, failure : (t : Throwable) -> Unit ) {
        enqueue(object: Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) = failure(t)

            override fun onResponse(call: Call<T>, response: Response<T>) = success(response)

        })

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

    @SuppressLint("PackageManagerGetSignatures")
    private fun getKeyHash() {
        try {
            val info: PackageInfo = packageManager.getPackageInfo("package com.example.user.kakaologin", PackageManager.GET_SIGNATURES)
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
                exception.printStackTrace()
                Log.i("OAuth_app", "${exception.printStackTrace()}")

            }
        }

        override fun onSessionOpened() {
            Log.i("OAuth_app", "onSessionOpened")
            requestMe()
//        }

        }
    }}



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