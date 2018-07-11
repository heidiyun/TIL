# 안드로이드 로그인 (Retrofit + Gson + Http)
> build.gradle 에 아래 코드 추가하기.
    implementation 'com.squareup.retrofit2:retrofit:2.4.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.4.0'

    implementation 'com.squareup.okhttp3:okhttp:3.10.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:3.10.0'


1. response의 정보를 객체로 변환 시킬 클래스 만든다. (model)

```java
data class Auth(
        @field:SerializedName("access_token") val accessToken: String,
        @field:SerializedName("token_type") val tokenType: String)

```

2. 로그인 인증 받을 때 필요한 api와 정보를 저장할 interface를 만든다. (annotation)

```java
interface AuthApi {
    @FormUrlEncoded // 보내는 형식
    @POST("login/oauth/access_token") // POST로 보낼 URL
    @Headers("Accept: application/json") // response 형식
    fun getAccessToken(@Field("client_id") clientId: String, 
                       @Field("client_secret") clientSecret: String,
                       @Field("code") code: String): Call<Auth>
    	// accesstoken을 받을 때 필요한 body
}
```

3. Retrofit의 Client은 자바에서 '싱글톤'으로 만들었으나,
   kotlin에서는 object를 통해서 싱글톤을 쉽게 만들 수 있다.
   또는, 전역 메소드 또는 전역 프로퍼티로 만들 수 있다.

```java
val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
} // 요청 및 응답의 정보를 저장하는 인터셉터. 
  // 인터셉터가 볼 정보의 LEVEL을 지정한다.

val httpClient: OkHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(loggingInterceptor)
}.build() // Http의 형식으로 서버와 통신하는 client

val authApi: AuthApi = Retrofit.Builder().apply {
    baseUrl("https://github.com/") 
    client(httpClient) // 클라이언트로 HttpClient를 지정함.
    addConverterFactory(GsonConverterFactory.create()) 
    // 서버의 응답을 json 형식으로 간단하게 변환해주는 converter
}.build().create(AuthApi::class.java) // 응답의 정보를 AuthApi 형식으로 저장.
```

https://thdev.tech/androiddev/2016/11/13/Android-Retrofit-Intro.html