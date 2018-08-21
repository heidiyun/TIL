package com.example.user.fragmentexample

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.logging.Level

val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

//객체를 생성하면서 함께 호출해야하는 초기화 구문이 있을 경우 사용한다.

val httpClient = OkHttpClient.Builder().apply {
    addInterceptor(httpLoggingInterceptor)
}.build()

val authApi = Retrofit.Builder().apply {
    baseUrl("https://github.com/")
    client(httpClient)
    addConverterFactory(GsonConverterFactory.create())
}.build().create(AuthApi::class.java)


interface AuthApi {
    @POST("login/oauth/authorize")
    @Headers("Accept: application/json")
    fun get
}
