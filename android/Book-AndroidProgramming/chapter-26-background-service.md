# 26. 백그라운드 서비스
> 사용자가 화면을 계속 볼 필요가 없을 경우 ex) 음악 재생  
> 서비스를 사용하자.  

## IntentService 생성하기.
> PhotoGallery에서는 검색 결과가 나오면 상태 바를 통해서 통지할 것이다.  

IntentService는 일종의 액티비티 같은 것이며, Context이다. (Service는 Context의 자식 클래스이다.)

Service의 Intent를 command라 한다.
각 command는 서비스가 어떤 일을 하도록 지시하는 명령이다. 

IntentService는 커맨드를 관리하는 큐(queue)를 가진다.
전달받은 커맨드 순서대로 실행시킨다. 
첫번째 커맨드를 전달받으면 백그라운드 스레드를 시작시킨다.
각각의 커맨드는 백그라운드 스레드에 대해 onHandleIntent(Intent)를 호출한다.
큐에 남은 커맨드가 없으면 서비스는 중단되고 소멸된다.

**서비스는 액티비티처럼 인텐트에 응답하므로 반드시 AndroidManifest.xml에 선언되어야 한다.**

```kotlin
/activity/?.startService(intent)
// 서비스 실행시키기.
```

### 안전한 백그라운드 네트워킹
안드로이드에서는 사용자가 백그라운드 애플리케이션의 네트워킹을 차단할 수 있는 기능이 있다.
따라서 백그라운드로 네트워킹을 한다면 ConnectivityManager를 사용해서 네트워크가 사용 가능한지 확인을 먼저 해야 한다!

```kotlin
private fun isNetworkAvailableAndConnected() : Boolean {
    val connectivityManager = getSystemService(Context./CONNECTIVITY_SERVICE/) as ConnectivityManager
    val isNetworkAvailable = connectivityManager./activeNetworkInfo/!= null

    return isNetworkAvailable && connectivityManager./activeNetworkInfo/./isConnected/
}
```

ConnectivityManager.activeNetworkInfo를 사용하기 위해서는 AndroidManifest.xml에 
`<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
를 추가한다.

## AlarmManager를 사용한 지연 실행
실행 중인 액티비티가 없어도 백그라운드 서비스를 사용할 수 있도록 해야 한다.

Handler를 사용해서 Handler.sendMessageDelayed(…) 나 Handler.postDelayed(…)를 호출하면 구현할 수 있다.
그러나 사용자가 액티비티를 꺼버리면 위와 같은 방법은 사용할 수 없다.
프로세스가 꺼지면 Handler 메시지도 없어지기 때문이다.

### AlarmManager
> Intent를 전달할 수 있는 시스템 서비스.  

PendingIntent를 사용하여 IntentService를 시작시키고 그것을 AlarmManager에 전달하면 된다. 

PollService(IntentService)에서 알람을 켜고 끄는 메소드.
```kotlin
fun setServiceAlarm(context: Context, isOn: Boolean) {
    val intent  = PollService.newIntent(context)
    val pendingIntent = PendingIntent.getService(context, 0, intent, 0)
// 인테트를 전달한 Context, 요청 코드, 전달할 Intent 객체

    val alarmManager = context.getSystemService(Context./ALARM_SERVICE/) as AlarmManager

    if (isOn)  alarmManager.setInexactRepeating(AlarmManager./ELAPSED_REALTIME/,
        SystemClock.elapsedRealtime(), POLL_INTERVAL, pendingIntent)
// 알림의 기준 시간 (현재시간)을 나태내는 상수, 알림이 시작되는 시간, 알람을 반복하는 시간 간격, 알람이 작동될 때 촉발시킬 PendingIntent
    else {
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
// 알람 취소.
    }
}
```

### 올바른 방법으로 알람 사용하기
백그라운드 작업을 일정한 시간 간격으로 반복 수행하면 장치의 배터리와 데이터 사용량을 많이 소모한다. 

알람 반복을 설정할 수 있는 메소드
1. AlarmManager.setRepeating(…)
2. AlarmManager.setInexactRepeating

안드로이드 킷캣(4.4) 이전까지는 setRepeating(…) 메소드에서 정확한 시간 간격으로 알람을 설정했다. 그리고 setInexactRepeating(...) 메소드에서는 부정확한 형태로 반복되도록 알람을 설정했다.
(INTERVAL_FIFTEEN_MINUTES,
INTERVAL_HALF_HOUR,
INTERVAL_HOUR,
INTERVAL_HALF_DAY,
INTERVAL_DAY) 시간 간격을 지정하여 정확한 반복으로 바뀌어 수행된다.

안드로이드 킷캣에서 부터 setRepeating, setInexactRepeating 모두 부정확한 반복으로 알람을 설정한다. 또한 사전 정의된 시간 간격 상수를 사용하는 제약이 없어졌다.

대신, AlarmManager.setWindow(…), AlarmManager.setExact(…) 메소드를 사용할 수 있다. 이 경우 정확한 반복의 알람을 설정할 수 있지만 알람이 한 번만 발생한다.

결과적으로 애플리케이션을 설치할 장치가 킷캣이상의 버전이라면 setRepeating(...) 메소드를 호출하면 된다. 

### 시간 기준 옵션
1. AlarmManager.ELAPSED_REALTIME
마지막으로 장치를 부팅한 이래로 경과된 시간을 기준으로 사용한다.
2. AlarmManager.RTC
세계시 기준의 시간을 기준으로 사용한다. 
항상 Locale 처리를 같이 구현해주어야 한다.

위의 두가지 시간 기준을 사용하면 장치의 화면이 꺼진 상태에 있으면 정해진 시간이 되어도 알람이 동작하지 않는다.

AlarmManager.ELAPSED_REALTIME_WAKEUP
AlarmManager.RTC_WAKEUP 중 하나를 사용하여 알람이 장치를 깨우도록 해야 한다. 권장하지는 않는다.

### PendingIntent
PendingIntent는 토큰(token) 객체이다. 
PendingIntent.getService(…)를 호출하여 객체를 하나 얻는 것은 “해당 인텐트를 startService(Intent)에 전달하고 싶으니 기억해주세요!”라고 운영체제에 말하는 것이다.
이후, PendingIntent 토큰의 send()를 호출할 수 있다. send()를 호출하면 안드로이드 운영체제에 Intent를 전달하는 것이다.

같은 Intent를 갖는 PendingIntent를 두 번 요청하면 동일한 PendingIntent 객체를 얻게되어, 존재 여부를 검사할 수 있다.

### PendingIntent를 사용해서 알람 관리하기.
각 PendingIntent에는 하나의 알람만 등록할 수 있다.

```kotlin
val pendingIntent = PendingIntent
    .getService(context, 0, intent, PendingIntent./FLAG_NO_CREATE/)
```

FLAT_NO_CREATE를 전달하면 PendingIntent의 존재 여부를 확인하여 존재하지 않으면 null을 반환한다.

#android/