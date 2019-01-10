package kr.ac.ajou.heidi.todolist

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
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

    @POST("/users")
    fun registerUser(@Body body: JsonObject): Call<Void>

    @PUT("update")
    fun updateUserInfo(@Header("Authorization") token: String,
                       @Body body: JsonObject): Call<Void>

    @POST("auth/login")
    @Headers("Accept: application/json")
    fun getAccessToken(@Body body: JsonObject): Call<Auth>

    @POST("images")
    @Headers("Accept: application/json")
    fun getSignedUrl(@Header("Authorization") token: String): Call<SignedUrl>

    @PUT
    fun postFile(@Url signedUrl: String, @Body file: RequestBody): Call<Void>

    @GET("images/do.jpeg")
    @Headers("Accept: application/json")
    fun getImageFile(): Call<RedirectUrl>

    @POST("/todo")
    fun postTodo(@Header("Authorization") token: String, @Body body: JsonObject): Call<String>

    @GET("/user")
    fun getUser(@Header("Authorization") token: String): Call<Todo>

    @GET("/todo")
    fun getTodolist(@Header("Authorization") token: String): Call<ArrayList<Todo>>

    @PUT("/todo")
    fun removeTodo(@Header("Authorization") token: String, @Body body: JsonObject): Call<Void>


}

data class Auth(@field:SerializedName("access_token") val accessToken: String)

data class SignedUrl(val signedUrl: String)

data class RedirectUrl(@field:SerializedName("redirect_url") val redirectUrl: String)





