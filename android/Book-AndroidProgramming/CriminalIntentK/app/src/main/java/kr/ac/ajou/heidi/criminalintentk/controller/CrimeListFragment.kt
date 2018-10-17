package kr.ac.ajou.heidi.criminalintentk.controller

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_crime_list.view.*
import kotlinx.android.synthetic.main.list_item_crime.view.*
import kr.ac.ajou.heidi.criminalintentk.R
import kr.ac.ajou.heidi.criminalintentk.model.Crime
import kr.ac.ajou.heidi.criminalintentk.model.CrimeLab

class CrimeListFragment : Fragment() {

    inner class CrimeViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_crime, parent, false)), View.OnClickListener {


        lateinit var crime: Crime
        init {
            this.itemView.setOnClickListener(this)

        }

        fun setCrimeObject(crime: Crime) {
            this.crime = crime
        }


        override fun onClick(view: View?) {

            val intent = CrimePagerActivity.newIntent(activity!!.applicationContext, crime.id)
            startActivityForResult(intent, 123)
        }

    }


    inner class CrimeViewAdapter : RecyclerView.Adapter<CrimeViewHolder>() {
        var crimes: List<Crime> = listOf()

        override fun onCreateViewHolder(parent: ViewGroup, position: Int): CrimeViewHolder = CrimeViewHolder(parent)

        override fun getItemCount(): Int = crimes.size

        override fun onBindViewHolder(crimeHolder: CrimeViewHolder, position: Int) {
            val crime = crimes[position]

            crimeHolder.setCrimeObject(crime)

            with(crimeHolder.itemView) {
                crimeTitleTextView.text = crime.title
                crimeDateTextView.text = crime.date.toString()
                crimeSolvedCheckBox.isChecked = crime.solved

            }
        }

    }

    private val adapter = CrimeViewAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        view.crimeRecyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        val crimeLab = CrimeLab.get()
        adapter.crimes = crimeLab.crimes
        view.crimeRecyclerView.adapter = adapter

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 123) {

        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()

    }

    private fun updateUI() {
        val crimeLab = CrimeLab.get()
        val crimes = crimeLab.crimes

        adapter.notifyDataSetChanged()

    }

}