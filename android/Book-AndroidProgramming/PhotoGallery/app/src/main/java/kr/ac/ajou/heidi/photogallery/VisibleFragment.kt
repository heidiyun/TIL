package kr.ac.ajou.heidi.photogallery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.fragment.app.Fragment

abstract class VisibleFragment: Fragment() {
    companion object {
        val TAG = VisibleFragment::class.java.name.toString()
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(PollService.ACTION_SHOW_NOTIFICATION)
        activity?.registerReceiver(onShowNotification, filter,
            PollService.PERM_PRIVATE, null)
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(onShowNotification)
    }

    private val onShowNotification = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i(TAG, "canceling notification")
//            resultCode = Activity.RESULT_CANCELED
        }
    }
}