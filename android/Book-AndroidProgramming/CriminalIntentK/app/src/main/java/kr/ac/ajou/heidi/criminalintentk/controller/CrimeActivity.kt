package kr.ac.ajou.heidi.criminalintentk.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import kr.ac.ajou.heidi.criminalintentk.R
import java.util.*

class CrimeActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID) as UUID
        return CrimeFragment.newInstance(crimeId)
    }

    companion object {
        private const val EXTRA_CRIME_ID = "kr.ac.ajou.heidi.crime_id"
        fun newIntent(packageContext: Context, crimeId: UUID): Intent {
            val intent = Intent(packageContext, CrimeActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            return intent
        }
    }

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
