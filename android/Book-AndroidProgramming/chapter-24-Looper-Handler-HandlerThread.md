# 24. Looper, Handler, HandlerThread
## Flickr에서 섬네일 다운로드
섬네일을 한꺼번에 다운로드 하는 것에 대한 문제점
1. 다운로드하는 시간이 오래 걸릴 수 있어서 다운로드가 끝날 때 까지 UI가 변경되지 않을 것이다.
2. 이미지 전체를 장치에 저장하는 데 따르는 비용문제
이미지가 무한정 보여주고 싶은 경우 메모리가 부족해 질 수 있다.

즉, 화면에 나타날 이미지만 다운로드해서 보여줘야한다.
이것은 RecyclerView의 Adapter에서 처리해주어야 한다.

AsyncTask는 백그라운드 스레드를 실행하는 가장 쉬운 방법이다.
그러나 반복적이면서 실행시간이 긴 작업을 하기에 적합한 스레드 모델은 아니다.

## 메인 스레드(main thread)와 소통하기
백그라운드 스레드에서 완료한 일을 메인 스레드에게 알려주어야 한다.
메인 스레드가 할 일이 있을 경우, 메인 스레드의 일이 끝날 때 까지 기다려야 한다.
이때, 사용하는 방법이 바로 메시지 큐(message queue)이다.

쉽게 설명하자면, 각 스레드 별로 메시지 함이 있다고 생각하자.
특정 스레드에게 알리고 싶은 일을 메시지 함에도 넣어준다.
해당 스레드가 현재 맡은 일을 완료하고 메시지 함에서 해야할 일을 꺼내서 수행한다.

메시지 큐를 사용하여 동작하는 스레드를 메시지 루프(message loop)라 하며, 그 스레드는 자신의 큐에서 새로운 메시지를 찾기 위해 반복해서 루프를 실행한다.

### 메시지 루프 (message loop)
하나의 스레드와 하나의 looper로 구성된다.
looper는 스레드의 메시지 큐를 관리하는 객체이다. 

**메인 스레드는 looper를 갖는 메시지 루프이다.**
메인 스레드가 하는 모든 일은 그것의 looper에 의해 수행된다.
looper는 메시지 큐에 있는 메시지들을 꺼내어 메시지가 지정하는 일을 수행한다

PhotoGallery 앱에서 메시지 루프이기도 한 백그라운드 스레드를 생성한다.
Looper를 준비해주는 HandlerThread 클래스를 사용한다.

## 백그라운드 스레드 (Background Thread) 만들기
```kotlin
class ThumbnailDownloader<T> : HandlerThread(TAG) {
    companion object {
        val TAG = "ThumbnailDownloader"
    }
    
    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "Got a URL: $url")
    }
}
```

하나의 제네릭 인자인 <T>가 클래스에 지정되어 있다.
ThumbnailDownloader를 사용하는 PhotoGalleryFragment에서는 객체 타입을 지정해줘야 한다. 객체는 각 다운로드를 식별하고 이미지가 다운로드된 다음에 해당 타입으로 변경할 UI요소를 결정하기 위해 필요하다.
제네릭 인자를 사용하면 특정 타입의 객체에만 클래스를 사용하도록 제한하지 않으므로 코드 구현이 더 유연해진다.

### ThumbnailDownloader thread 사용 시 고려해야 할 것.
1. 
```kotlin
thumbnailDownloader.start()
thumbnailDownloader./looper/
```
start()를 호출하여 스레드를 시작시킨 후에 looper를 호출 한다.
이것은 스레드가 준비되었는지 확인하는 방법이다. 
looper를 호출하기 전까지는 onLooperPrepared()가 호출되었다는 보장이 없다.
따라서 queueThumbnail(…)을 호출할 때 null Handler가 되어 실행이 중단될 가능성이 있다.
2. 
```kotlin
override fun onDestroy() {
    super.onDestroy()
    thumbnailDownloader.quit()
}
```
onDestroy() 내부에서 quit()을 호출시켜 스레드를 중단시킨다.
중단시키지 않는다면, 앱이 종료되어도 계속 실행되어 성능에 악영향을 끼친다.

3. 
```kotlin
override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
    val galleryItem = galleryItems[position]
    /with/(holder.itemView) *{*
val placeholder = ContextCompat.getDrawable(/context/, R.drawable./bill_up_close/)
        fragmentPhotoGalleryImageView.setImageDrawable(placeholder)
        thumbnailDownloader.queueThumbnail(holder, galleryItem.url)
    *}*
}

```
PhotoAdapter.onBindViewHolder(…)에서 ThumbnailDownloader 스레드의 queueThumbnail() 메서드를 호출하도록 변경한다.

## 메시지와 메시지 핸들러
### 메시지 구조
Message는 인스턴스이며, 필드를 가진다.

1. what : 메시지를 나타내는 사용자 정의 정수 값
2. obj :  메시지와 함께 전달되는 사용자 지정 객체
3. target : 메시지를 처리할 Handler

Message를 생성하면 자동으로 Handler에 연결된다.
Message가 처리될 준비가 되면 Handler가 message를 담당하는 객체가 된다.

### 핸들러 구조
메시지를 사용해서 실제 작업을 하려면 Handler의 인스턴스가 필요하다.
Handler는 Message를 처리하는 대상일 뿐만 아니라 Message를 생성하고 게시하는 인터페이스의 역할도 한다.

**Message는 반드시 Looper로부터 게시되고 사용되어야 한다.**
Looper는 Message 객체들의 메시지 수신함을 소유하기 때문이다.
그래서 항상 Handler는 항상 자신의 동료인 Looper의 참조를 갖는다.

**Handler는 하나의 Looper에 연결된다.**
Message는 하나의 목표 Handler에 연결된다.

다수의 Message들이 동일한 대상 Handler를 참조할 수 있다.
다수의 Handler가 하나의 Looper에 연결될 수 있다.

### 핸들러 사용하기
Message를 처리하는 대상 Handler는 사용자가 직접 설정하지 않는다.
Message는 Handler.obtainMessage(…)를 사용하여 생성하는 것이 좋다.
이때 다른 메시지 필드들을 인자로 전달하면 이 메서드가 호출된 Handler 객체를 대상 핸들러로 설정해준다.

Message는 Handler.obtainMessage(…)는 매번 새로운 Message 객체의 생성을 피하기 위해 공유되는 재활용 풀에서 Message 객체를 가져다 사용한다.

Message 객체를 얻고 Handler에 전달하기 위해서 sendToTarget()을 호출한다.
Handler는 전달받은 Message를 Looper의 메시지 큐 끝에 넣는다.

PhotoGallery의 queueThumbnail() 메소드 내부에서 메시지 객체를 얻은 후 대상 핸들러에 전달할 것이다. 
이때 메시지의 what 필드는 MESSAGE_DOWNLOAD로 정의된 상수가 될 것이다. 
obj 필드는 다운로드를 식별하는데 사용될 T 타입의 객체가 될 것이다.
애플리케이션에서는 adapter가 queueThumbnail()에 인자로 전달한 PhotoHolder 객체가 obj 필드 값이 된다.


#android/책