# HTTP 통신
> 데이터를 조회하거나 저장하기 위해 서버와 통신해야 할 때 대부분 HTTP API를 사용한다.

* URL 접속을 통해 데이터를 읽어오는 방법이다.
* 안드로이드의 특성상 외부 데이터베이스에 직접 접근할 수 가 없어 중간 매체인 WEB을 활용한다.

메인스레드는 UI스레드라고도 하며 액티비티 내에서 일어나는 모든 처리를 담당한다.
오랜 시간 동안 수행하는 작업을 메인 스레드가 하게되면 다른 UI가 업데이트 되지 않는다. 이를 방지하기위해 비동기 처리를 해야한다. 
동기 처리를 하게 되면 android.os.NetworkOnMainThreadException!!이 발생한다.

* Http Protocol
POST/GET/DELETE/PUT..

1. 인터넷에 대한 권한을 받아와야 한다.
[AndroidManifest.xml]
```
	<uses-permission android:name="android.permission.INTERNET" />
```

http://mailmail.tistory.com/13

2. HttpLoggingInterceptor가 필요하다.
요청 및 응답 정보를 기록하는(로그 기능) 것.
```java
	val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
    // 어느 정보만을 볼 것인지.
}
```

3. OkHttpClient가 필요하다.
서버와 통신할 Client로, HttpLoggingInterceptor를 추가해주어야 한다.
```java
	val httpClient: OkHttpClient = OkHttpClient.Builder().apply {
    	addInterceptor(loggingInterceptor)
	}.build()
```

# android manifest 

android:launchMode = "singleTask" : 하나만 수행을 한다. (반복적으로 같은 창을 띄우지 않는다.)

android:host="authorize"    
android:scheme="gitclient" />  https:// 역할을 하는 부분.

# access token 얻어오기
> access token을 통해서 github의 여러 서비스를 이용할 수 있다.
in postman

https://github.com/login/oauth/access_token
Body - {
	"client_id": "d3be1425f2b68349c8cf",
	"client_secret": "8f604dacb00a15456bd8854fe003874d33287c30",
	"code": "934ec8a5e4a38d2452b5"
}

Content-Type = application/json
Accept = application/json

결과 : {
    "access_token": "767bdd9334234c8c3d",
    "token_type": "bearer",
    "scope": ""
}