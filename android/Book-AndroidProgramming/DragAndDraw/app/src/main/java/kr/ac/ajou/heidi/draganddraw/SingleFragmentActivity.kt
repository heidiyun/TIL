package kr.ac.ajou.heidi.draganddraw

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class SingleFragmentActivity: AppCompatActivity() {

    abstract fun createFragment(): androidx.fragment.app.Fragment

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