package kr.ac.ajou.heidi.photogallery

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat

class PollService : IntentService(TAG) {
    companion object {
        const val TAG = "PollService"
        const val POLL_INTERVAL = 1000 * 10L
        const val ACTION_SHOW_NOTIFICATION = "kr.ac.ajou.heidi.photogallery.SHOW_NOTIFICATION"
        const val PERM_PRIVATE = "kr.ac.ajou.heidi.photogallery.PRIVATE"
        const val REQUEST_CODE = "REQUEST_CODE"
        const val NOTIFICATION = "NOTIFICATION"

        fun newIntent(context: Context): Intent = Intent(context, PollService::class.java)

        fun setServiceAlarm(context: Context, isOn: Boolean) {
            val intent = PollService.newIntent(context)
            val pendingIntent = PendingIntent.getService(context, 0, intent, 0)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (isOn) alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(), POLL_INTERVAL, pendingIntent
            )
            else {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
            QueryPreferences.setAlarmOn(context, isOn)
        }

        fun isServiceAlarmOn(context: Context): Boolean {
            val intent = PollService.newIntent(context)
            val pendingIntent = PendingIntent
                .getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE)
            return pendingIntent != null
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (!isNetworkAvailableAndConnected()) return

        val query = QueryPreferences.getStoredQuery(this)
        val lastResultId = QueryPreferences.getLastResultId(this)

        val items = if (query == null) {
            FlickrFetchr().fetchRecentPhotos()
        } else {
            FlickrFetchr().searchPhotos(query)
        }

        if (items.size == 0) return

        val resultId = items[0].id
        if (resultId == lastResultId) Log.i(TAG, "Got an old result: $resultId")
        else {
            Log.i(TAG, "Got a new result: $resultId")
            val newIntent = PhotoGalleryActivity.newIntent(this)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val notificationChannel =
                    NotificationChannel("photogallery", "photogallery", NotificationManager.IMPORTANCE_DEFAULT)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(true)
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                notificationManager.createNotificationChannel(notificationChannel)
                val builder = Notification.Builder(this, "photogallery")
                    .setContentTitle("got a new picture")
                    .setContentText("새로운 사진을 확인하세요.")
                    .setSmallIcon(R.drawable.notification_icon_background)
                    .setAutoCancel(true)
                    .build()
//                notificationManager.notify(0, builder)
                showBackgroundNotification(0, builder)
                sendBroadcast(Intent(ACTION_SHOW_NOTIFICATION), PERM_PRIVATE)

            } else {
                val pendingIntent = PendingIntent.getActivity(this, 0, newIntent, 0)
                val notification = NotificationCompat.Builder(this)
            }


        }

        QueryPreferences.setLastResultId(this, resultId)
    }

    private fun showBackgroundNotification(requestCode: Int, notification: Notification) {
        val intent = Intent(ACTION_SHOW_NOTIFICATION)
        intent.putExtra(REQUEST_CODE, requestCode)
        intent.putExtra(NOTIFICATION, notification)
        sendOrderedBroadcast(intent, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null)
    }

    private fun isNetworkAvailableAndConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isNetworkAvailable = connectivityManager.activeNetworkInfo != null

        return isNetworkAvailable && connectivityManager.activeNetworkInfo.isConnected
    }

}