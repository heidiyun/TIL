package com.example.user.kakaologin

import android.content.Context
import android.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

fun okHttpClient(context: Context) = OkHttpClient.Builder().apply {
    addInterceptor(AuthInterceptor(context))
}.build()

const val KEY_AUTH_TOKEN = "kr.ac.ajou.yun"

fun updateToken(context: Context, accessToken: String?) {
    PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(KEY_AUTH_TOKEN, accessToken).apply()
}

fun getToken(context: Context): String? {
    return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_AUTH_TOKEN, null)
}

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val original = chain.request()

        val request = original.newBuilder().apply {
            getToken(context)?.let { token ->
                addHeader("Authorization", "bearer $token")
            }
        }.build()

        return chain.proceed(request)
    }

}

fun provideUserApi(context: Context) = Retrofit.Builder().apply {
    baseUrl("https://openapi.naver.com/")
    client(okHttpClient(context))
    addConverterFactory(GsonConverterFactory.create())
}.build().create(UserApi::class.java)


interface UserApi {
    @GET("v1/nid/me")
    @Headers("Accept: application/json")
    fun getUserInfo(): Call<UserInfo>

}

data class UserInfo(val response: Response)
data class Response(val name: String)


