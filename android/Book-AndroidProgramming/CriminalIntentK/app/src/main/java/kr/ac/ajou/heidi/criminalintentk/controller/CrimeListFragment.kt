package kr.ac.ajou.heidi.criminalintentk.controller

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import kotlinx.android.synthetic.main.fragment_crime_list.view.*
import kotlinx.android.synthetic.main.list_item_crime.view.*
import kr.ac.ajou.heidi.criminalintentk.R
import kr.ac.ajou.heidi.criminalintentk.model.Crime
import kr.ac.ajou.heidi.criminalintentk.model.CrimeLab

class CrimeListFragment : Fragment() {

    inner class CrimeViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
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
        var crimes: ArrayList<Crime> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup, position: Int)
                : CrimeViewHolder = CrimeViewHolder(parent)

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

    companion object {
        const val SAVED_SUBTITLE_VISIBLE = "subtitle"
    }

    private val adapter = CrimeViewAdapter()
    private var subtitleVisible: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        savedInstanceState?.let {
            subtitleVisible = it.getBoolean(SAVED_SUBTITLE_VISIBLE)
        }

        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        view.crimeRecyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        val crimeLab = CrimeLab.get(view?.context ?: return view)
        view.crimeRecyclerView.adapter = adapter
        adapter.crimes = crimeLab?.crimes ?: return view

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_new_crime -> {
                val crime = Crime()
                CrimeLab.get(view?.context ?: return false)?.addCrime(crime)
                view?.let {
                    val intent = CrimePagerActivity.newIntent(it.context, crime.id)
                    startActivity(intent)
                }
                return true
            }

            R.id.menu_item_show_subtitle -> {
                subtitleVisible = !subtitleVisible
                activity?.invalidateOptionsMenu()
                updateSubtitle()
                return true
            }

            else -> {
                super.onOptionsItemSelected(item)
                return false
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_crime_list, menu)

        val subtitleItem = menu?.findItem(R.id.menu_item_show_subtitle) ?: return
        if (subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle)
        } else {
            subtitleItem.setTitle(R.string.show_subtitle)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 123) {

        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun updateUI() {

        CrimeLab.get(view?.context ?: return)?.let {
            adapter.crimes = it.crimes

        }
        adapter.notifyDataSetChanged()

        updateSubtitle()
    }

    private fun updateSubtitle() {
        val crimeLab = CrimeLab.get(view?.context ?: return)
        val crimeCount = crimeLab?.crimes?.size ?: 0
        var subtitle: String? = resources.getQuantityString(
                R.plurals.subtitle_plural, crimeCount, crimeCount
        )

        if (!subtitleVisible) {
            subtitle = null
        }

        val activity = activity as AppCompatActivity
        activity.supportActionBar?.subtitle = subtitle

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, subtitleVisible)
    }
}