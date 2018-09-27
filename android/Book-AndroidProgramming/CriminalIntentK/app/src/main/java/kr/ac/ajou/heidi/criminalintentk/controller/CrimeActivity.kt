package kr.ac.ajou.heidi.criminalintentk.controller

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import kr.ac.ajou.heidi.criminalintentk.R

class CrimeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime)

        var fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        if (fragment == null) {
            fragment = CrimeFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit()
        }


    }
}
