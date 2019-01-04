# 28. 웹과 웹뷰 브라우징
PhotoGallery 내부에서 웹 콘텐트 보여주기.
1. WebView
2. 디바이스의 브라우저 앱 사용

## 암시적 인텐트를 사용한 브라우저 앱 띄우기
## WebView 사용하여 사진 띄우기
```kotlin
class PhotoPageFragment : VisibleFragment() {
    companion object {
        val ARG_URI = "photo_page_url"

        fun newInstance(uri: Uri): Fragment {
            val args = Bundle()
            args.putParcelable(ARG_URI, uri)

            val fragment = PhotoPageFragment()
            fragment./arguments/= args
            return fragment
        }
    }

    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uri = /arguments/?.getParcelable(ARG_URI)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val £ = inflater.inflate(R.layout./fragment_photo_page/, container, false)
        view.fragmentPhotoPageWebView./settings/./javaScriptEnabled/= true
        view.fragmentPhotoPageWebView./webViewClient/= object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }

        view.fragmentPhotoPageWebView.loadUrl(uri./toString/())

        return view
    }


}

```

webView의 getSettings()를 호출하여 WebSettings의 인스턴스를 얻은 후 WebSettings.setJavaScriptEnabled를 true로 설정하여 자바스크립트를 사용할 수 있다. 
WebSettings는 WebView를 변경할 수 있는 세 가지 방법 중 첫 번째다.

WebViewClient는 이벤트 인터페이스이며, 렌더링 이벤트에 응답할 수 있다.
즉, renderer가 특정 URL로부터 이미지를 로드하기 시작할 때 그것을 감지할 수 있다.
또는 HTTP POST 요청을 서버에 다시 전송할지 여부를 결정할 수 있다.

shouldOverrideUrlLoading 메소드는 새로운 URL이 WebView에 로드될 때 무엇을 할지 알려준다. 만일 true를 반환하면 ‘이 URL을 처리하지마, 내가 처리할게’라고 알려주는 것이다. false를 반환하면 WebView가 URL을 로드하라고 알려주는 것이다.

그러나 이 메소드는 암시적 인텐트를 촉발시켜 모바일 버전의 웹 사이트로 연결한다. 따라서, defaultWebViewClient를 사용하면 암시적 인텐트로 인해 사용자의 디폴트 웹 브라우저가 실행된다.

슈퍼 클래스에 디폴트로 구현된 메소드를 오버라이드하여 false를 반환하면 WebView로 보여준다.

### WebChromeClient 사용하기.
ProgressBar를 코드와 연결하기 위해 WebView의 두 번째 콜백인 WebChromeClient를 사용할 것이다. 
WebViewClient는 렌더링 이벤트에 응답하는 인터페이스며, WebChromeClient는 브라우저 주변의 chrome 요소들을 변경시키는 이벤트에 반응하는 인터페이스다. 
이 인터페이스에는 자바스크립트 경고, 파비콘(브라우저 아이콘), 페이지 로딩 진척도와 현재 페이지의 제목 변경 등이 포함된다. 

```kotlin
override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout./fragment_photo_page/, container, false)

    progressBar = view.fragmentPhotoPageProgressBar
    view.fragmentPhotoPageProgressBar./max/= 100
    view.fragmentPhotoPageWebView./settings/./javaScriptEnabled/= true

    view.fragmentPhotoPageWebView./webChromeClient/= object: WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (newProgress == 100) {
                progressBar?./visibility/= View./GONE/
} else {
                progressBar?./visibility/= View./VISIBLE/
progressBar?./progress/= newProgress
            }
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            val activity = /activity/as AppCompatActivity
            activity./supportActionBar/?./subtitle/= title

        }
    }

    view.fragmentPhotoPageWebView./webViewClient/= object: WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return false
        }
    }

    view.fragmentPhotoPageWebView.loadUrl(uri./toString/())


    return view
}
```

## WebView에서의 올바른 방향 전환 처리
장치를 회전시키면 WebView가 웹 페이지를 다시 로드한다.
WebView는 많은 데이터를 가지고 있어, onSaveInstanceState(...)에서 모두 저장할 수 없다.

PhotoPageFragment를 유보하는 것이 해결책이 될 수 있다고 생각하겠지만,
WebView가 뷰 게층구조의 일부여서 장치의 방향이 바뀔 때 마다 소멸되었다가 생성된다.

WebView나 VideoView와 같은 클래스들의 경우에 AndroidManifest.xml에 android:configChanges 속성을 지정하여 액티비티 자체적으로 구성 변경을 처리하게  할 수 있다. 

```xml
<activity android:name=".PhotoPageActivity"
    android:configChanges="keyboardHidden|orientation|screenSize"/>
```

### 구성 변경 처리의 위험성
액티비티 스스로 구성변경을 처리하는 것은 위험하다.

1.  리소스 수식자가 자동으로 동작하지 않는다.
개발자가 직접 뷰를 다시 로드해야 한다.

2. Activity.onSaveInstanceState(…)의 오버라이딩을 소홀히 할 수 있다.
그러나 일시적인 UI 상태 정보를 저장하기 위해 메소드의 오버라이딩은 필요하다.
메모리 부족으로 인해 액티비티가 소멸 및 재생성되는 것을 대비해야 한다.
(액티비티가 실행 상태가 아닐 때는 시스템에 의해 언제든지 소멸될 수 있다.)

## 자바스크립트 객체 추가하기
## 킷캣의 WebView 기능 향상
안드로이드 크롬 앱과 동일한 렌더링 엔진을 공유하므로 일관된 인터페이스와 기능을 갖는 웹 페이지를 사용할 수 있다.
크롬 개발 도구를 사용한 WebView의 원격 디버깅 지원이 가능하다.
WebView.setWebContentsDebuggingEnabled()를 호출하여 활성화할 수 있다.
애플리케이션이 API 19 킷캣 이전 장치를 지원하는 경우, WebView의 일부 동작이 다르다는 것을 알아두자.



#android/책