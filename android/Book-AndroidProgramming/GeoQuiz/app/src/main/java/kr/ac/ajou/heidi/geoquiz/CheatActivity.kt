package kr.ac.ajou.heidi.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_cheat.*
import kr.ac.ajou.heidi.geoquiz.CheatActivity.Companion.EXTRA_ANSWER_IS_TURE
import kr.ac.ajou.heidi.geoquiz.CheatActivity.Companion.IS_ANSWER_SHOWN

class CheatActivity : AppCompatActivity() {

    companion object {
       const val EXTRA_ANSWER_IS_TURE = "kr.ac.ajou.heidi.quizapp.answer_is_true"
        const val IS_ANSWER_SHOWN = "kr.ac.ajou.heidi.quizapp.is_answer_shown"
    }

    var isAnswerShown = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        savedInstanceState?.let{
            setAnswerShownResult(it.getBoolean(IS_ANSWER_SHOWN))
        }

        val answer = intent.getBooleanExtra("answer", false)

        cheatButton.setOnClickListener {
            if (answer) answerTextView.text = getString(R.string.true_button)
            else answerTextView.text = getString(R.string.false_button)
            isAnswerShown = true
            setAnswerShownResult(isAnswerShown)
        }
    }

    fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent()
        data.putExtra(IS_ANSWER_SHOWN, isAnswerShown)
        setResult(Activity.RESULT_OK, data)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.i("CheatActivity2", "onSaveInstanceState")
        outState?.putBoolean(IS_ANSWER_SHOWN, true)
    }
}

fun newIntent(packageContext: Context, answer: Boolean): Intent {
    val intent = Intent(packageContext, CheatActivity::class.java)
    intent.putExtra(EXTRA_ANSWER_IS_TURE, answer)
    return intent
}

fun wasAnswerShown(result : Intent) : Boolean{
    Log.i("CheatActivity", "${result.getBooleanExtra(EXTRA_ANSWER_IS_TURE, false)}")
    return result.getBooleanExtra(IS_ANSWER_SHOWN, false)
}
