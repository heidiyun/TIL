package kr.ac.ajou.heidi.locatr

import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class LocatrActivity : SingleFragmentActivity() {
    companion object {
        const val REQUEST_ERROR = 0
    }
    override fun createFragment(): Fragment = LocatrFragment.newInstance()

    override fun onResume() {
        super.onResume()
        val errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)

        if (errorCode != ConnectionResult.SUCCESS) {
            val errorDialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(this, errorCode, REQUEST_ERROR) { finish() }
            errorDialog.show()
        }
    }
}
