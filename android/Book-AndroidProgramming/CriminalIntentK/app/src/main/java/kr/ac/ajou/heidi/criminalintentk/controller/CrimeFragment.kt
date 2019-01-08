package kr.ac.ajou.heidi.criminalintentk.controller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import kotlinx.android.synthetic.main.fragment_crime.*
import kotlinx.android.synthetic.main.fragment_crime.view.*
import kr.ac.ajou.heidi.criminalintentk.R
import kr.ac.ajou.heidi.criminalintentk.model.Crime
import kr.ac.ajou.heidi.criminalintentk.model.CrimeLab
import kr.ac.ajou.heidi.criminalintentk.model.dateFormat
import java.util.*

class CrimeFragment : Fragment() {

    companion object {
        const val ARG_CRIME_ID = "crime_id"
        fun newInstance(crimeId: UUID): Fragment {
            val args = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)
            val fragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }

        const val DIALOG_DATE = "DialogDate"
        const val REQUEST_DATE = 0
    }

    lateinit var crime: Crime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = arguments?.getSerializable(ARG_CRIME_ID)!! as UUID
        crime = CrimeLab.get(activity?.applicationContext ?: return)?.getCrime(crimeId)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        view.crimeTitle.setText(crime.title)
        view.crimeTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = s.toString()
            }
        })

        view.crimeDateButton.text = dateFormat.format(crime.date)

        view.crimeDateButton.setOnClickListener {
            val dialog = DatePickerFragment.newInstance(crime.date)
            dialog.setTargetFragment(this@CrimeFragment, REQUEST_DATE)
            dialog.show(fragmentManager, DIALOG_DATE)

        }

        view.crimeSolved.isChecked = crime.solved

        view.crimeSolved.setOnCheckedChangeListener { _, isChecked ->
            crime.solved = isChecked
        }

        returnResult()

        return view
    }

    override fun onPause() {
        super.onPause()

        CrimeLab.get(context ?: return)?.updateCrime(crime)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK)
            return

        if (requestCode == REQUEST_DATE) {
            val date = data?.getSerializableExtra(DatePickerFragment.EXTRA_DATE)
            crime.date = date as Date
            updateDate()
        }
    }

    fun returnResult() {
        activity?.setResult(Activity.RESULT_OK, null)
    }

    private fun updateDate() {
        crimeDateButton.text = dateFormat.format(crime.date)
    }
}
