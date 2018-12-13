package kr.ac.ajou.heidi.photogallery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class StartupReceiver : BroadcastReceiver() {

    companion object {
        val TAG = StartupReceiver::class.java.name
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "Received broadcast intent: ${intent?.action}")
        context?.let {
            val isOn = QueryPreferences.isAlarmOn(context)
            PollService.setServiceAlarm(context, isOn)
        }
    }

}