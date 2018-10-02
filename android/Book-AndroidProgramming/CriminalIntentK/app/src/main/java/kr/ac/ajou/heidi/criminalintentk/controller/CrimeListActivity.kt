package kr.ac.ajou.heidi.criminalintentk.controller

import android.support.v4.app.Fragment

class CrimeListActivity: SingleFragmentActivity() {
    override fun createFragment(): Fragment = CrimeListFragment()

}