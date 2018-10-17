package kr.ac.ajou.heidi.criminalintentk.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_crime_pager.*
import kr.ac.ajou.heidi.criminalintentk.R
import kr.ac.ajou.heidi.criminalintentk.model.CrimeLab
import java.util.*

class CrimePagerActivity : FragmentActivity() {

    companion object {
        const val EXTRA_CRIME_ID = "kr.ac.ajou.heidi.crime_id"
        fun newIntent(packageContext: Context, crimeId: UUID): Intent {
            val intent = Intent(packageContext, CrimePagerActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)



        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID)

        val crimes = CrimeLab.get().crimes
        activityCrimePagerViewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                val crime= crimes.get(position)
                return CrimeFragment.newInstance(crime.id)
            }

            override fun getCount(): Int = crimes.size

        }

        for (i in 0 until crimes.size) {
            if (crimes.get(i).id == crimeId) {
                activityCrimePagerViewPager.currentItem = i
                break

            }
        }
    }
}