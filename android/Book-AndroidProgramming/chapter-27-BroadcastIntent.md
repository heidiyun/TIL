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


#android/책