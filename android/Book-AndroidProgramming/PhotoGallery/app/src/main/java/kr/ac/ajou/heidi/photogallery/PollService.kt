package kr.ac.ajou.heidi.photogallery

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.SystemClock
import android.util.Log

class PollService : IntentService(TAG) {
    companion object {
        const val TAG = "PollService"
        const val POLL_INTERVAL = 1000 * 60L

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
        else Log.i(TAG, "Got a new result: $resultId")

        QueryPreferences.setLastResultId(this, resultId)
    }

    private fun isNetworkAvailableAndConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isNetworkAvailable = connectivityManager.activeNetworkInfo != null

        return isNetworkAvailable && connectivityManager.activeNetworkInfo.isConnected
    }

}