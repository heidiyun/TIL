package kr.ac.ajou.heidi.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cheat.*
import kr.ac.ajou.heidi.geoquiz.CheatActivity.Companion.EXTRA_ANSWER_IS_TURE

class CheatActivity : AppCompatActivity() {

    companion object {
       const val EXTRA_ANSWER_IS_TURE = "kr.ac.ajou.heidi.quizapp.answer_is_true"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        val answer = intent.getBooleanExtra("answer", false)

        cheatButton.setOnClickListener {
            if (answer) answerTextView.text = getString(R.string.true_button)
            else answerTextView.text = getString(R.string.false_button)
            setAnswerShownResult(true)
        }
    }

    fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent()
        data.putExtra(EXTRA_ANSWER_IS_TURE, isAnswerShown)
        setResult(Activity.RESULT_OK, data)
    }
}

fun newIntent(packageContext: Context, answer: Boolean): Intent {
    val intent = Intent(packageContext, CheatActivity::class.java)
    intent.putExtra(EXTRA_ANSWER_IS_TURE, answer)
    return intent
}

fun wasAnswerShown(result : Intent) : Boolean{
    return result.getBooleanExtra(EXTRA_ANSWER_IS_TURE, false)
}
