# 27. 브로드캐스트 인텐트
### PhotoGallery 개선할 점.
1. 장치를 부팅한 이후에 앱을 실행한 적이 없더라도 새로운 검색 결과를 받아와서 새로운 결과가 있으면 사용자에게 통지한다. 
2. 사용자가 우리 앱을 사용하지 않을 때만 새로운 결과에 대한 통지를 알려준다.

## 일반 인텐트 vs 브로드캐스트 인텐트
애플리케이션에 어떤 이벤트가 발생하면, 안드로이드는 브로드캐스트 인텐트를 사용해서 해당 이벤트를 모든 컴포넌트에 알려준다. 
일반 인텐트와의 차이점은 브로드캐스트 인텐트는 수신자로 등록한 여러 액티비티나 서비스가 동시에 받을 수 있다. 

## 장치 부팅 시 앱 깨우기
PhotoGallery 백그라운드 알람은 장치가 다시 재시작되면 동작하지 않을 것이다.
장치가 부팅된 후에 스스로를 시작시켜야 된다.
시스템에서는 장치의 전원이 켜질 때마다 BOOT_COMPLETED 액션을 갖는 브로드캐스트 인텐트를 전송한다.
이 인텐트를 리스닝하는 브로드캐스트 수신자를 생성하고 등록해야 한다.

### 브로드캐스트 수신자를 생성하고 등록하기.
#### 독자적 수신자 (standalone receiver)
AndroidManifest.xml에 등록되며, 프로세스가 종료되더라도 시작될 수 있다.
#### 동적 수신자 (dynamic receiver)
코드에서 등록하며, 애플리케이션의 특정 액티비티나 프래그먼트가 실행되어야만 시작할 수 있다.

서비스나 액티비티처럼 브로드캐스트 수신자는 시스템에 등록되어야 동작한다.

```kotlin
class StartupReceiver: BroadcastReceiver() {

    companion object {
        val TAG = StartupReceiver::class./java/./name/
}

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "Received broadcast intent: ${intent?./action/}")
    }

}
```

```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>

<receiver android:name=".StartupReceiver">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED"/>
    </intent-filter>
```

인텐트 필터에 선언된 브로드캐스트 인텐트가 전송되면 언제든 해당 수신자가 시작될 수 있다. 
수신자가 시작되면 onReceive(Context, Intent) 메소드가 실행되고 종료된다.

브로드캐스트 수신자의 onReceive(Context, Intent) 메소드는 5초 이내에 필요한 일을 수행하고 복귀하여 수신자를 끝내야 한다. 브로드캐스트 인텐트는 여러 브로드캐스트 수신자에게 동시 전파되므로 각 수신자가 시스템 리소스를 많이 사용하지 않게 해야 한다. 

### 브로드캐스트 수신자 사용하기
브로드캐스트 수신자를 짧은 시간 동안만 살아있으므로 할 수 있는 일에 제약이 따른다.
1. 비동기 API를 사용할 수 없다.
2. 시간이 오래 걸리는 일을 수행할 수 없다. (네트워킹, 스토리지 사용)

PhotoGallery의 수신자에서는 알림이 켜져 있는지 여부를 확인하여 알람을 설정할 것이다.

## 포그라운드 통지의 필터링
PhotoGallery의 통지를 사용자가 앱을 사용하고 있을 때에는 안오게 하고 싶다.
1. 커스텀 브로드캐스트 인텐트를 보내고 받는다. 
PhotoGallery의 컴포넌트에서만 받을 수 있게 제한한다.
2. 브로드캐스트 수신자를 매니페스트가 아닌 코드에서 동적으로 등록한다. 
3. 여러 수신자에게 차례대로 데이터를 전달하기 위해 순차 브로드캐스트를 전송한다. (특정 수신자가 마지막에 실행되도록 하기 위해서)

### 브로드캐스트 인텐트 전송하기
인텐트를 생성하고 sendBraodcast(Intent)의 인자로 전달한다.
액션을 정의해야 전달해야 한다. -> 액션 상수.
```kotlin
class PollService : IntentService(TAG) {
	companion object {
		const val ACTION_SHOW_NOTIFICATION = 
  		"kr.ac.ajou.heidi.photogallery.SHOW_NOTIFICATION"
	}

	override fun onHandleIntent(intent: Intent?) {
		/.../
		notificationManager.notify(0, builder)
		sendBroadcast(Intent(ACTION_SHOW_NOTIFICATION))
		}
}
```

### 동적 수신자를 생성하고 등록하기.
ACTION_SHOW_NOTIFICATION 브로드캐스트 인텐트의 수신자가 필요하다.
PhotoGalleryFragment가 살아있는 동안에만 인텐트를 수신하게 할 것이기 때문에 AndroidManifest.xml에 등록하지 않고 동적으로 생성할 것이다.
(독자적 인텐트를 생성하면 PhotoGalleryFragment가 살아있는지를 확인하는 추가코드를 작성해야 한다.)

수신자를 등록할 때는 registerReceiver(BraodcastReceiver, IntentFilter)를 호출하고, 해지할 때는 unregisterReceiver(BroadcastReceiver)를 호출한다.
 
VisibleFragment를 생성하여 포그라운드 통지를 감출 것이다.

```kotlin
abstract class VisibleFragment: Fragment() {
    companion object {
        val TAG = VisibleFragment::class./java/./name/.toString()
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(PollService.ACTION_SHOW_NOTIFICATION)
        /activity/?.registerReceiver(onShowNotification, filter)
    }

    override fun onStop() {
        super.onStop()
        /activity/?.unregisterReceiver(onShowNotification)
    }

    private val onShowNotification = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context,
                "Got a broadcast: ${intent?./action/}",
                Toast./LENGTH_LONG/).show()
        }

    }
}
```
XML로 지정할 수 있는 어떤 IntentFilter라도 코드로 나타낼 수 있다.
addCategory(String), addAction(String), addDataPath(String)

### private permission 사용하기.
브로드캐스트 인텐트를 사용하면 수신자 외에도 시스템의 다른 컴포넌트들이 리스닝하여 동작할 수 있다. 
이를 방지하기 위해서는 2가지 방법이 있다.

1. 수신자가 애플리케이션 내부에 있다고 AndroidManifest에 등록한다.
android:exported=“false”

2. 사용자 정의 퍼미션을 생성하면 된다.
```xml
<permission android:name="kr.ac.ajou.heidi.photogallery.PRIVATE"
            android:protectionLevel="signature"/>
<uses-permission android:name="kr.ac.ajou.heidi.photogallery.PRIVATE"/>
```

```kotlin
//PollService
const val PERM_PRIVATE = "kr.ac.ajou.heidi.photogallery.PRIVATE"

sendBroadcast(Intent(ACTION_SHOW_NOTIFICATION), PERM_PRIVATE)
```

퍼미션을 사용하려면 sendBroadcast 인자로 전달해야 한다.

브로드캐스트 수신자를 실행하기 위해 외부에서 브로드캐스트 인텐트를 생성할 수 있다.
이를 방지하기 위해서 수신자에도 퍼미션을 전달하자.

```kotlin
// VisibleFragment
/activity/?.registerReceiver(onShowNotification, filter,
    PollService.PERM_PRIVATE, null)
```

## 보호 수준
모든 커스텀 퍼미션에는 android:protectionLevel의 값을 지정해야 한다.

#### normal 
앱을 설치하기 전에 해당 퍼미션을 볼 수 있다.
사용자의 승인을 받지 않는다. 
#### dangerous 
normal로 사용하지 않는 모든 것들의 보호 수준.
사적인 데이터의 액세스, 네트워크를 통한 데이터 전송이나 수신 등
사용자의 승인을 명시적으로 요청한다.
#### signature
퍼미션을 선언한 앱과 동일한 인증서 서명된 앱인 경우 사용이 가능하다.
퍼미션이 승인되어도 사용자에게 알리지 않는다.
앱의 내부적인 기능에 사용하는 퍼미션
#### signatureOrSystem
퍼미션을 선언한 앱과 동일한 인증서로 서명된 앱이거나, 단말 출시시 특정 폴더에 저장되어 출시된 앱인 경우 사용 가능하다.
안드로이드 시스템 이미지의 모든 패키지에도 퍼미션을 승인한다.
시스템 이미지에 내장된 앱들과 소통하는데 사용된다. 

### 순차 브로드캐스트 인텐트로 결과 받기
동적으로 등록되는 수신자가 다른 수신자보다 항상 먼저 PollService.ACTION_SHOW_NOTIFICATION 브로드캐스트 인텐트를 수신하도록 해야 한다.
그리고 통지가 게시되지 않아야 한다는 것을 나타내기 위해 브로드캐스트 인텐트를 변경해야 한다.

일반 브로드캐스트 인텐트는 모든 수신자가 동시에 수신한다. 실제로는 onReceive()가 메인 스레드에서 호출되므로 수신자들이 동시에 실행되지는 않는다. 그러나, 수신자를 어떤 특정 순서로 실행되게 하는 것은 불가능하며, 실행이 끝난 시점도 알 수 없다.
즉, 브로드캐스트 수신자들 상호간의 소통은 쉽지 않으며, 인텐트 전송자가 수신자로부터 어떤 정보를 받는 것도 어렵다.

순차(ordered) 브로드캐스트 인텐트를 사용하면 양방향 소통을 구현할 수 있다. 
브로드캐스트 전송자가 특정 브로드캐스트 수신자를 전달함으로써, 수신자들로부터 결과를 받을 수 있게 해준다.  -> result receiver(결과 수신자)

PhotoGallery에서는 수신자의 반환 값을 변경하여 통지를 취소할 것이다.

```kotlin
override fun onReceive(context: Context?, intent: Intent?) {
    Log.i(TAG, "canceling notification")
    /resultCode/= Activity./RESULT_CANCELED/
//sendResultCode(Activity.RESULT_CANCELED)
}
```

더 복잡한 데이터를 반환하려면 setResultData(String), setResultExtras(Bundle)을 사용하자.
Int, String, Bundle을 모두 사용하고 싶다면 setResult(Int, String, Bundle)을 사용하자.

* 순차 브로드캐스트 인텐트 전송하기
```kotlin
val intent = Intent(ACTION_SHOW_NOTIFICATION)
intent.putExtra(REQUEST_CODE, requestCode)
intent.putExtra(NOTIFICATION, notification)
sendOrderedBroadcast(intent, PERM_PRIVATE, null, null, Activity./RESULT_OK/, null, null)
```
Context.sendOrderedBroadcast(Intent, String, BroadcastReceiver, Handler, Int, String, Bundle)
세번째 인자부터, 결과 수신자, 결과 수신자를 실행할 Handler, 결과 코드의 초기 값, 결과 데이터, 순차 브로드캐스트 인텐트의 결과 엑스트라 객체

결과 수신자는 순차 브로드캐스트 인텐트를 받는 모든 다른 수신자가 실행된 후에 마지막으로 실행될 것이다. 

## 수신자와 오래 실행되는 태스크
Main 루프의 실행 시간 제약보다 더 오랫동안 실행되는 태스크로 브로드캐스트 인텐트 시작시키기.

1. 브로드캐스트 인텐트를 시작시키는 일을 코드로 서비스에 넣은 다음, 브로드캐스트 수신자에서 해당 서비스를 시작시키는 것이다.
**장점**
서비스는 요청을 서비스할 필요가 있는 한 계속 실행될 수 있다. 
여러 요청들을 큐에 넣고 차례대로 관리할 수 있다.
2. BroadcastReceiver.goAsync() 메소드를 사용하는 것이다.
결과를 제공하는데 사용될 수 있는 BroadcastReceiver.PendingResult 객체를 반환한다.
이 PendingResult 객체를 AsyncTask에 전달하여 더 오래 실행되는 일을 수행할 수 있다. 그 다음,  PendingResult의 메소드를 호출하여 브로드캐스트 인텐트에 응답하면 된다.
**장점**
순차 브로드캐스트의 결과를 설정할 수 있다. 
**단점**
여전히 5초 정도 이내로만 브로드캐스트 인텐트를 처리해야 한다.


## 로컬 이벤트
브로드캐스트 인텐트는 전역적인 형태로 시스템 전체에 걸쳐 정보를 전파할 수 있다.
그런데 우리 앺의 프로세스 내부에만 발생 이벤트를 전파하고 싶다면 이벤트 버스(event bus)를 사용하자.

이벤트 버스는 공유 버스나 데이터 스트림의 개념으로 동작하며, 애플리케이션의 컴포넌트가 구독할 수 있다. 이벤트가 버스에 게시되면 이벤트를 구독하는 컴포넌트가 시작되고 그것의 콜백 코드가 실행된다.

EventBus는 서드파티 이벤트 버스 라이브버리다.
Square의 Otto, RxJava  Subjects의 Observables를 사용하는 방법이 있다.

안드로이드에서 제공하는 LocalBroadcastManager도 있지만, 서드파티 라이브버리가 더 유연하고 쉬운 API를 제공한다.

[EventBus 사용법](https://github.com/greenrobot/EventBus)

## 프래그먼트의 가시성 검출하기
PhotoGallery에서 LocalBroadcastManager을 사용하지 않은 이유
1. 브로드캐스트 인텐트를 동기화하는 sendBroadcastSync(Intent) 메소드를 제공하는데, 순차 브로드캐스트 인텐트를 제공하지 않는다. 
2.  sendBroadcastSync(Intent) 메소드에서 별개의 스레드에서 브로드캐스트 인텐트를 전송하거나 수신할 수 없다. PhotoGallery의 경우에는 백그라운드 스레드이 PollService.onHandleIntent()에서 브로드캐스트 인텐트를 전송해야 한다. 그리고 main 스레드에서 그 인텐트를 수신한다.


#android/책