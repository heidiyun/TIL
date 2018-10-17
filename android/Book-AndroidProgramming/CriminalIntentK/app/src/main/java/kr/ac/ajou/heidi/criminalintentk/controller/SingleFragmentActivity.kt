package kr.ac.ajou.heidi.criminalintentk.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import kr.ac.ajou.heidi.criminalintentk.R
import java.util.*

abstract class SingleFragmentActivity: FragmentActivity() {

    abstract fun createFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime)

        var fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        if (fragment == null) {
            fragment = createFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit()
        }

    }
}