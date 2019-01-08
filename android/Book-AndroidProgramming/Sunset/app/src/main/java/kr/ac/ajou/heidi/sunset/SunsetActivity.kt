package kr.ac.ajou.heidi.sunset

import androidx.fragment.app.Fragment

class SunsetActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return SunsetFragment.newInstance()
    }
}
