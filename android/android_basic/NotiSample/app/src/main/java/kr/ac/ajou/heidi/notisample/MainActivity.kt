package kr.ac.ajou.heidi.notisample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val channelId = "main"
        val channelName = "main channel"
        val channelDescription = "main channel description"
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = channelDescription
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)



        notiButton.setOnClickListener {
            val notiIcon =
                    BitmapFactory.decodeResource(resources, R.drawable.ic_star)

            val pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    Intent(applicationContext, SubActivity::class.java),
                    PendingIntent.FLAG_CANCEL_CURRENT
            )


            val builder =
                    NotificationCompat
                            .Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_star)
                            .setContentTitle("노티제목")
                            .setContentText("노티내용")
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setLargeIcon(notiIcon)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)

            val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, builder.build())

        }
    }
}
