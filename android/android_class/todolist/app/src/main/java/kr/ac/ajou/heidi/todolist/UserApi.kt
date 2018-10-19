package kr.ac.ajou.heidi.todolist

import com.google.gson.JsonObject
import okhttp3.OkHttpClient
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
    baseUrl("http://10.0.2.2:3000/")
    client(okHttpClient)
    addConverterFactory(GsonConverterFactory.create())
}.build().create(MongoApi::class.java)

interface MongoApi {

    @POST("users")
    @Headers( "Accept: application/json", "Content-Type: application/json")
    fun createUser(@Body body: JsonObject): Call<CreatedUserInfo>

    @POST("users/update")
    @Headers( "Accept: application/json", "Content-Type: application/json")
    fun updateUser(@Body body: JsonObject): Call<CreatedUserInfo>

    @POST("users/delete")
    @Headers( "Accept: application/json", "Content-Type: application/json")
    fun deleteUser(@Body body: JsonObject): Call<DeletedInfo>
    //delete에 body를 못넣는다고 나옴.
    // get 배열로 어떻게 받냐.

    @GET("users")
    @Headers( "Accept: application/json")
    fun getUserInfo(): Call<Array<CreatedUserInfo>>

}

data class CreatedUserInfo(val name: String, val email: String)

data class DeletedInfo(val n: Int)

