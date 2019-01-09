# 31. 위치와 플레이 서비스
> 이번 장에서는 사용자의 현재 위치를 찾은 후 인근 사진들을 플리커에서 검색하는 앱을 만들 것이다.  

## 위치와 라이브러리
안드로이드 표준 라이브러리 패키지닝ㄴ android.location에는 기본적인 위치 기반 서비스를 제공하는 API가 포함되어 있다. (ex. LocationManager)
이 API를 사용하면 다양한 소스로부터 지리적 위치 데이터를 얻을 수 있다.
대부분의 폰에서는 GPS나 기지국 또는 와이파이 연결이 소스가 된다.

그러나 android.location API는 안드로이드 초창기부터 제공된 것으로 정확도도 떨어지고 위치를 찾느라 배터리도 많이 소모된다.
현재 안드로이드에서는  이 API를 사용하지 말고 구글 플레이 서비스의 구글 위치 서비스 API를 사용할 것을 권장한다.

외부에서는 GPS가 가장 좋은 위치 데이터 소스며 정확도도 높다.
정확도는 떨어지지만 내부에서는 기지국 신호가 사용될 수 있다.
두 가지 신호를 모두 찾을 수 없을 때는 장치에 내장된 가속도계와 자이로스코프를 사용할 수 있다.

### 구글 플레이 서비스
구글 플레이 서비스의 일부인 구글 위치 서비스 API는 우리 앱에서 현재 위치를 알 수 있는 더 좋은 기능을 제공한다. 사용하기 쉽고 정확도가 높으며 배터리 소모도 적다.

1. Fused Location Provider API (위치 추적)
2. Geofencing API (사용자가 특정 범위에 진입하거나 벗어나는 경우 알려줌)
3. Activity Recognition API (가속도계 센서와 기기의 자율 학습 기능을 통해 사용자의 움직임을 추적함.)

구글 플에이 서비스는 구글에서 제공하는 다양한 서비스들을 쉽게 사용할 수 있게 만든 클라이언트 라이브러리이며, 구글 플레이 스토어와 함께 앱의 형태로 제공되어 장치에 설치된다.
그리고 구글 플레이 스토어를 통해 자동 업데이트를 지원하므로 안드로이드 버전이나 기타 다른  제약에 관계 없이 최신 기능을 쉽고 빠르게 사용할 수 있다.
따라서 구글 플레이 스토어가 설치된 장치에서만 사용이 가능하다.

## 플레이 서비스와 위치 테스트를 위한 에뮬레이서 준비하기
설치된 안드로이드 SDK에 에뮬레이터의 최신 시스템 이미지를 추가해야 한다.
시스템 이미지에는 실제 안드로이드 운영체제와 유사하게 리눅스 커널과 안드로이드 프레임워크 컴포넌트들이 들어있다.
따라서 에뮬레이터를 생성하고 실행하면 실제 장치처럼 컴퓨터에서 안드로이드 운영체제가 실행되는 셈이다.

Tools -> SDK Manager -> SystemImage 설치 확인
 
에뮬레이터가 실행중이라면 반드시 종료 후 다시 시작을 해야 한다.

### 모의 위치 데이터
에뮬레이터에서는 모의(mock) 위치 데이터가 필요하다. 

터미널에서 abd install [MockWalker.apk 경로] MockWalker.apk

## 구글 플레이 서비스 설정하기
구글 플레이 서비스를 사용하기 위해서 구글 플레이 서비스 라이브러리를 app 모듈의 의존성에 추가한다.
서비스 자체는 구글 플레이 앱에 있지만, 코드는 플레이 서비스 라이브러리에 포함되어 있다.
[Set Up Google Play Services  |  Google APIs for Android       |  Google Developers](https://developers.google.com/android/guides/setup) 를 참고하자.

**플레이 서비스 사용 가능 여부 확인**
```kotlin
class LocatrActivity : SingleFragmentActivity() {
    companion object {
        const val REQUEST_ERROR = 0
    }

    override fun onResume() {
        super.onResume()
        val errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)

        if (errorCode != ConnectionResult./SUCCESS/) {
            val errorDialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(this, errorCode, REQUEST_ERROR) *{*finish() *}*
errorDialog.show()
        }
    }
}
```

### 위치 관련 퍼미션
```xml
AndroidManifest.xml

<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
// GPS
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
// WIFI

```

## 구글 플레이 서비스 사용하기
서비스를 사용하려면 플레이 서비스 클라이언트를 생성해야 한다.
GoogleApiClient 클래스의 인스턴스이다. 
[API reference  |  Android Developers](https://developer.android.com/reference/) 참고.

클라이언트를 생성하기 위해 GoogleApiClient.Builder를 생성하고 구성한다.
이때 우리가 사용할 특정 API를 갖는 인스턴스를 구성한 후 build()를 호출하여 인스턴스를 생성한다.

```kotlin
/* LocatrFragment */
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
    /activity/?./let/*{*
client = GoogleApiClient
            .Builder(*it*)
            .addApi(LocationServices./API/)
            .build()
    *}*
}
```

GoogleApiClient.Builder 인스턴스를 생성하고 인스턴스에 위치 서비스 API를 추가하는 코드를 작성한다.
onStart()에서 클라이언트 연결하고, onStop()에서 클라이언트 연결을 해제하는 것을 권장한다.

```kotlin
override fun onStart() {
    super.onStart()
   
    /activity/?.invalidateOptionsMenu()
    client?.connect()
}

override fun onStop() {
    super.onStop()
    
    client?.disconnect()
}
```
클라이언트 연결 여부에 따라 메뉴 상태 변경

연결 상태 정보는  두 개의 콜백 인터페이스, ConnectionCallbacks와 OnConnectionFailedListener를 통해서 전달된다.
클라이언트가 연결되었을 때 메뉴 항목을 변경하기 위해 ConnectionCallbacks 리스너를 구현하고 activity.invalidateOptionsMenu()를 호출합니다.

```kotlin
/* LocatrFragment */
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
    /activity/?./let/*{*
client = GoogleApiClient
            .Builder(*it*)
            .addApi(LocationServices./API/)
            .addConnectionCallbacks(object: GoogleApiClient.ConnectionCallbacks {
                override fun onConnectionSuspended(p0: Int) {
                }

                override fun onConnected(p0: Bundle?) {
                    /activity/?.invalidateOptionsMenu()
                }

            })
            .build()
    *}*
}
```

## 플리커 사이트 이미지의 지리 데이터 검색
안드로이드에서는 Location 객체가 위치 데이터를 갖는다.(경도 위도)
FlickrFetchr가 가지고 있는 기존 buildUrl을 오버로딩한다.
location으로 생성된 url로 사진을 찾는다. (searchPhotos 오버로딩)

```kotlin
/* FlickrFetchr */
private fun buildUrl(location: Location) :String =
        ENDPOINT.buildUpon()
            .appendQueryParameter("method", SEARCH_METHOD)
            .appendQueryParameter("lat", location./latitude/.toString())
            .appendQueryParameter("lon", location./longitude/.toString())
            .build().toString()

private fun searchPhotos(location: Location): ArrayList<GalleryItem> {
    val url = buildUrl(location)
    return downloadGalleryItems(url)
}

```

## 위치 데이터 얻기
Fused Location Provider API의 시작 인터페이스는  FusedLocationProviderApi다.
위치 서비스의 시작 클래스는 LocationServices며, 이 클래스에서는 FusedLocationProviderApi를 구현하는 싱글톤 객체 참조를 갖는 FusedLocationApi라는 필드를 갖고 있다. 

FusedLocationApi 필드를 통해서 Fused Location Provider의 위치 데이터를 얻으려면 위치 요청(location request)를 생성해야 한다. 
```kotlin
val request = LocationRequest.create()
request./priority/= LocationRequest./PRIORITY_HIGH_ACCURACY/
request./numUpdates/= 1
// 위치가 업데이트 되는 횟수
request./interval/= 0
// 위치가 업데이트 되는 주기
```
최초로 LocationRequest 객체가 생성될 때는 도시의 한 구획 내에서 정확도 우선으로 구성되며, 종결 시간까지 느린 업데이트가 반복된다.
위의 코드에서는 위치가 확정되는 대로 바로 업데이트 한다는 의미이다. 

위의 request를 전송한 후 수신되는 Location 정보를 처리하는 Location Listener를 등록해야 한다.

## 플리커에서 이미지 찾아 보여주기
플리커 사이트로부터 위치 데이터에 인접한 곳의 사진을 찾아 다운로드하고 보여주는 AsyncTask를 구현할 것이다.

AsyncTask의 서브 클래스인 SearchTask를 LocatrFragment의 내부 클래스로 추가한다.
```kotlin
/* Locatr Fragment */

LocationServices.getFusedLocationProviderClient(/activity/?: return)
    ./lastLocation/.addOnSuccessListener *{*location *->*
SearchTask().execute(location)
*}*

inner class SearchTask : AsyncTask<Location, Unit, Unit>() {
    private var galleryItem: GalleryItem? = null
    override fun doInBackground(vararg params: Location?) {
        val fetchr = FlickrFetchr()
        params[0]?./let/*{*
val items = fetchr.searchPhotos(*it*)
            if (items.isEmpty())  return
            galleryItem = items[0]
        *}*
}

}
// 위치 데이터 인접한 곳의 GalleryItem 찾기
```

찾은 GalleryItem과 연관된 이미지 데이터를 다운로드하고 Bitmap 객체로 변환한다.
onPostExecute() 메소에서 imageView를 보여준다.

```kotlin
/* SearchTask */
override fun doInBackground(vararg params: Location?) {
   ...
try {
        val bytes = fetchr.getUrlBytes(galleryItem?.url ?: "")
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } catch (e: IOException) {
        Log.i(LocatrFragment::class./java/./name/, "Unable to download bitmap $e")
    }
}

override fun onPostExecute(result: Unit?) {
    /view/?.photoImageView?.setImageBitmap(bitmap)
}

```


#android/책