package kr.ac.ajou.heidi.criminalintentk.controller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_crime.view.*
import kotlinx.android.synthetic.main.fragment_crime_list.*
import kotlinx.android.synthetic.main.list_item_crime.view.*
import kr.ac.ajou.heidi.criminalintentk.R
import kr.ac.ajou.heidi.criminalintentk.model.Crime
import kr.ac.ajou.heidi.criminalintentk.model.CrimeLab
import org.jetbrains.anko.support.v4.toast

class CrimeListFragment : Fragment() {

    inner class CrimeViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_crime, parent, false)), View.OnClickListener {

        override fun onClick(v: View?) {
            toast("click!!!!")
        }

    }

    inner class CrimeViewAdapter(): RecyclerView.Adapter<CrimeViewHolder>() {
        var crimes: List<Crime> = listOf()

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CrimeViewHolder = CrimeViewHolder(parent)

        override fun getItemCount(): Int = crimes.size

        override fun onBindViewHolder(crimeHolder: CrimeViewHolder, position: Int) {
            val crime = crimes[position]
            with(crimeHolder.itemView) {
                crimeTitleTextView.text = crime.title
                crimeDateTextView.text = crime.date.toString()
                crimeSolvedCheckBox.isChecked = crime.solved

            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        crimeRecyclerView.layoutManager = LinearLayoutManager(activity)
        val crimeLab = CrimeLab.get()
        val adapter = CrimeViewAdapter()
        adapter.crimes = crimeLab.crimes
        crimeRecyclerView.adapter = adapter

        return view
    }
}