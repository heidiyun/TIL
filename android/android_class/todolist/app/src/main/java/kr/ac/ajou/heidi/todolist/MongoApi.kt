package kr.ac.ajou.heidi.todolist

import android.content.Context
import android.graphics.Bitmap
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers
import java.io.File
import java.util.concurrent.TimeUnit

val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val okHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(httpLoggingInterceptor)
    connectTimeout(30, TimeUnit.SECONDS)
    readTimeout(30, TimeUnit.SECONDS)

}.build()


val mongoApi = Retrofit.Builder().apply {
    baseUrl("http://192.168.219.105:3000/")
    client(okHttpClient)
    addConverterFactory(GsonConverterFactory.create())
}.build().create(MongoApi::class.java)

interface MongoApi {

    @POST("auth/login")
    @Headers("Accept: application/json")
    fun getAccessToken(@Body body: JsonObject): Call<Auth>

    @POST("images")
    @Headers("Accept: application/json")
    fun getSignedUrl(@Header("Authorization") token: String): Call<SignedUrl>

    @PUT
    fun postFile(@Url signedUrl: String, @Body file: RequestBody): Call<Void>

//    @GET("users")
//    @Headers( "Accept: application/json")
//    fun getUserInfo(): Call<Array<CreatedUserInfo>>

}

data class Auth(@field:SerializedName("access_token") val accessToken: String)

data class SignedUrl(val signedUrl : String)

//class AuthInterceptor(private val context: Context): Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val original = chain.request()
//
//        val request = original.newBuilder().apply {
//            getToken(context)?.let {
//                token -> addHeader("Authorization", "bearer $token")
//            }
//        }.build()
//
//        return chain.proceed(request)
//    }
//
//}

