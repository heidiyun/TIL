package kr.ac.ajou.heidi.photogallery

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        val TAG = NotificationReceiver::class.java.name.toString()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "received result : $resultCode")
        if (resultCode != Activity.RESULT_OK) return

        intent ?: return
        val requestCode = intent.getIntExtra(PollService.REQUEST_CODE, 0)
        val notification = intent.getParcelableExtra(PollService.NOTIFICATION) as Notification
        context?.let {
            val notificationManager = NotificationManagerCompat.from(it)
            notificationManager.notify(requestCode, notification)
        }
    }
}