# 23. HTTP & 백그라운드 태스크
## 네트워킹 기본
```kotlin
fun getUrlBytes(urlSpec: String): ByteArray {
    val url = URL(urlSpec)
		// URL 객체를 생성한다.
    val connection = url.openConnection() as HttpURLConnection
		// URL을 참조하는 연결 객체를 생성한다.
    try {
        val bos = ByteArrayOutputStream()
        val inputStream = connection./inputStream/
		// HttpURLConnection은 연결을 나타낸다.
		// 그러나 getInputStream() 또는 getOutputStream()을 호출할 떄까지는 실제로 연결하지 않는다

if (connection./responseCode/!= HttpURLConnection./HTTP_OK/){
            throw IOException("${connection./responseMessage/}:with $urlSpec")
        }

        val buffer = ByteArray(1024)
        var bytesRead = inputStream.read(buffer)
        while (bytesRead > 0) {
            bos.write(buffer, 0, bytesRead)
            bytesRead = inputStream.read(buffer)
        }
        bos.close()
        return bos.toByteArray()

    } finally {
        connection.disconnect()
    }

}

```

### 네트워크 퍼미션 요청하기
AndroidManifest.xml에 
<uses-permission android:name=“android.permission.INTERNET”/> 
을 추가하자

안드로이드 6.0 마시멜로 버전부터 일반 퍼미션인 경우 앱을 설치할 때 승인받고,
사용자의 개인정보와 관련한 위험 퍼미션은 앱이 실행될 때 최초 한 번 사용자의 승인을 받아야 한다. (런타임 퍼미션)

## AsyncTask를 사용해서 백그라운드 스레드로 실행하기
> 백그라운드 스레드로 코드를 동작시키는 가장 쉬운 방법 -> AsyncTask 유틸리티 클래스를 사용하는 것.  

AsyncTask는 백그라운드 스레드를 생성한 후 스레드의 doInBackground(…) 메소드에 있는 코드를 실행한다.
운영체제가 제공하는 스레드 자원에 대하여 사용자가 신경쓰지 않아도 되므로 쉽고 안전하게 스레드를 사용할 수 있다. 

```kotlin
class PhotoGalleryFragment : Fragment() {

    private inner class FetchItemsTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            try {
                val result = FlickrFetchr().getUrlString("http://www.naver.com")
                Log.i(TAG, "Fetched contents of URL: $result")
            } catch (ioe: IOException) {
                Log.e(TAG, "Failed to fetch URL: $ioe")
            }
            
            return null
        }

    }

    companion object {
        val TAG = PhotoGalleryFragment::class./java/./name/.toString()
        fun newInstance(): PhotoGalleryFragment {
            return PhotoGalleryFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /retainInstance/= true
        FetchItemsTask().execute()
    }
```
execute()를 실행하면 AsyncTask가 시작된다. AsyncTask는 백그라운드 스레드를 시작시키고 donInBackground(…)를 호출한다. 

## 메인 스레드 (Main Thread)
Main Thread 에서 네트워킹 작업은 시간이 오래 걸려 UI가 중단되는 현상이 발생할 수 있으므로  허용하지 않는다.
Main Thread 에서 네트워킹 작업을 하게 되면 NetworkOnMainThreadException 예외를 발생시킨다. 

스레드는 단일의 실행 시퀀스이다. 하나의 스레드에서 실행되는 코드는 한 단계씩 실행된다.
모든 안드로이드 앱은 main 스레드로 시작된다. 그러나 main 스레드는 미리 정해진 실행 단계들로 되어 있지 않다. 대신에 무한 루프에 머물면서 사용자나 시스템에 의해 촉발된 이벤트를 기다린다. 특정 이벤트가 발생하면 해당 이벤트에 응답하는 코드를 실행한다.

Main 스레드는 UI를 변경하는 모든 코드를 실행한다. 이벤트 루프에서 UI 코드를 순서대로 유지한다. 따라서 코드가 실행되는 동안 상호 충돌이 생기지 않게 한다. 

## AsyncTask 클린업하기
PhotoGallery 애플리케이션에서는 AsyncTask 인스턴스를 추가로 유지 관리할 필요가 없도록 하기 위해 setRetainInstance를 호출하여 프래그먼트를 유보시켰다.
장치의 구성이 변경되어 새로운 AsyncTask 인스턴스가 생성되어 JSON 데이터를 다시 가져오는 일이 없도록 하기 위해서이다. 

이보다 더 복잡하게 사용하는 경우에는 AsynTask 스레드를 인스턴스 변수에 저장할 것이다. 그런 다음 AsyncTask.cancel(boolean 메소드를 호출하여 실행중인 스레드를 중단시킬 수 있다.

AsyncTask.cancel(boolean)은 두 가지 방식으로 동작할 수 있다.
1.  cancel(false) 호출 -> isCancelled() 의 결과가 true
doInBackground 내부에서 isCancelled를 확인하여 백그라운 스레드 실행을 정상적으로 종료한다.
2.  cancel(true) 호출 -> doInBackground 가 실행중인 백그라운드 스레드가 무조건 중단됨. 
이 방법을 권장하지 않는다.

AsyncTask 작업을 중단시키지 않는다면 메모리 누수 문제가 생길 수 있다.

## AsyncTask에 관해 추가로 알아보기
```kotlin
AsyncTask<String, Void, Void>>

fun doInBackground(vararg params: String?) {
}

AsyncTask.execute("First parameter", "Second parameter", "Etc.")

```
첫 번째 매개변수 : execute() 메소드의 인자로 전달하는 입력 매개변수의 타입을 지정할 수 있다. 해당 매개변수는 doInBackground(...)가 받는 매개변수의 타입이 된다. 

두 번째 매개변수에는 작업 진척 정도를 전달하기 위한 타입을 지정할 수 있다. 
```
AsyncTask<Void, Integer, Void>>

fun onProgressUpdate(vararg values: Integer?) {
}
// 작업의 정도를 프로그레스바를 이용하여 보여줄 수 있다. 
```

백그라운드 프로세스 내부에서 UI 변경을 할 수 없기 때문에 AsyncTask에서는 publishProgress(...)와 onProgressUpdate(...) 메소드를 제공한다.

doInBackground(…)로부터 publishProgress(…)를 호출한다. 그러면 onProgressUpdate(…)가 UI 스레드에서 호출된다. 
onProgressUpdate(…) 내부에서 UI를 변경할 수 있다. 

## AsyncTask의 대안
AsyncTask를 사용해서 데이터를 로드할 때 장치의 구성 변경이 생기면 생명주기를 관리하고 데이터를 보존해야 한다.
이때 Fragment의 setRetainInstance = true를 사용하면 편리하다.

AsyncTask를 실행했던 프래그먼트가 장치의 메모리 부족으로 인해 안드로이드 운영체제에 의해 소멸되는 경우 등, 사용자가 코드로 처리해야 할 때가 있다.

### Loader
> 데이터 소스로부터 데이터를 로드하기 위해 설계됨  
> 데이터 소스 : 디스크, 데이터베이스, 네트워크..등  

로더는 Android 3.0부터 도입된 것으로, 액티비티 또는 프래그먼트에서 비동기식으로 데이터를 쉽게 로드할 수 있게 한다.

#### 특성
* 모든 Activity와 Fragment에 사용될 수 있습니다.
* 데이터의 비동기식 로딩을 제공합니다.
* 데이터의 출처를 모니터하여 그 콘텐츠가 변경되면 새 결과를 전달합니다.
* 구성 변경 후에 재생성된 경우, 마지막 로더의 커서로 자동으로 다시 연결됩니다. 따라서 데이터를 다시 쿼리하지 않아도 됩니다.

AsyncTaskLoader는 데이터를 로드하는 작업을 다른 스레드로 인계하기위해 AsyncTask를 사용한다.

AsyncTaskLoader는 main 스레드를 방해하지 않고 데이터를 가져오는 작업을 수행하여 필요한 곳에 전달해준다.

장치의 구성이 변경될 때 컴포넌트 로더들의 생애와 데이터는 LoaderManager가 유지관리한다. 따라서 이미 데이터 로딩을 끝낸 로더를 장치 구성 변경이 생긴 후에 초기화하면 LoaderManager가 그 데이터를 즉시 전달할 수 있다. 이때 데이터 소스로부터 데이터를 다시 가져오지 않는다.
또한 이 작업을 프래그먼트 유보 여부와 상관없이 수행되므로 편리하다. 

#android/책