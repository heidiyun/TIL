package kr.ac.ajou.heidi.sunset

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class SingleFragmentActivity: AppCompatActivity() {

    abstract fun createFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        var fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        if (fragment == null) {
            fragment = createFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit()
        }

    }
}