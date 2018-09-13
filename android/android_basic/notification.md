# Notification
애플리케이션 최상단의 상태바를 통해 사용자에게 표시하는 메시지.

1. NotificationCompat.Builder 생성하기.
```java
 val builder =
        NotificationCompat
                .Builder(this, channelId)
                .setSmallIcon(R.drawable.*ic_star*)
                .setContentTitle("노티제목")
                .setContentText("노티내용")
//  setSmallIcon(ResId: Int), setContentTitle(title: String),
// setContentText(text: String)은 Notification 객체를 생성할 때 필요한 필수 메소드이다.

// NotificationCompat.Builder(context: Context)는 더이상 사용하지 못한다. 
// 그래서 channelId를 만들어줘야 한다.

val channelId = "main"
// 채널의 id
val channelName = "main channel"
// 채널의 이름
val channelDescription = "main channel description"
val channel = NotificationChannel(channelId, channelName, NotificationManager.*IMPORTANCE_DEFAULT*)
// NotificationChannel은 android api 26 부터 사용할 수 있다.
channel.*description*= channelDescription
// 채널의 사용자에게 보여줄 설명을 설정한다.
channel.enableLights(true)
// 채널에 게시 된 알림에 색을 부여할 것인지 설정한다.
channel.*lightColor*= Color.*RED*
// 채널에 게시 된 알림의 색을 지정한다.
channel.enableVibration(true)
// 진동을 사용할 것인지에 대해 설정한다.
channel.*vibrationPattern*= *longArrayOf*(100, 200, 300)
// 진동패턴을 설정한다.
val manager = getSystemService(Context.*NOTIFICATION_SERVICE*) as NotificationManager
manager.createNotificationChannel(channel)

```

NotificationChannel 추가 속성: [NotificationChannel  |  Android Developers](https://developer.android.com/reference/android/app/NotificationChannel)

2. Notification Builder에 필요한 메소드를 추가로 구현한다.
```java
val notiIcon =
        BitmapFactory.decodeResource(*resources*, R.drawable.*ic_star*)

 val builder =
        NotificationCompat
                .Builder(this, channelId)
                .setSmallIcon(R.drawable.*ic_star*)
                .setContentTitle("노티제목")
                .setContentText("노티내용")
                .setDefaults(Notification.*DEFAULT_SOUND*)
					//기본 알림 옵션 설정
                .setLargeIcon(notiIcon)
					// 알림에 표시 될 큰 아이콘 설정
                .setPriority(NotificationCompat.*PRIORITY_DEFAULT*)
					// 알림 통지의 우선순위 설정
                .setAutoCancel(true)
					// 사용자가 패널에서 알림을 클릭하면 자동으로 게시를 취소할 것인지에 대한 설정

```

Notification.Builder 추가 속성: [NotificationCompat.Builder  |  Android Developers](https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder)

3. 패널에 게시된 notification을 누르면 할 일 지정하기.
여기서는 특정 Activity가 실행되게 만들것이다.

```java
val pendingIntent = PendingIntent.getActivity(
        this,
        0,
        Intent(*applicationContext*, Activity::class.*java*),
        PendingIntent.*FLAG_CANCEL_CURRENT*
)

/*
public static PendingIntent getActivity(Context context, int requestCode,
        Intent intent, @Flags int flags) {
    return *getActivity*(context, requestCode, intent, flags, null);
}
*/

// 만든 pendingIntent를 NotificationBuilder에 추가한다.
val builder =
        NotificationCompat
                .Builder(this, channelId).
 					...
                .setContentIntent(pendingIntent)

```

4. 만든 Notification을 등록한다
```java
val notificationManager =
        getSystemService(Context.*NOTIFICATION_SERVICE*) as NotificationManager
notificationManager.notify(0, builder.build())

```

notification에 대한 android developer 문서: [알림  |  Android Developers](https://developer.android.com/guide/topics/ui/notifiers/notifications?hl=ko)