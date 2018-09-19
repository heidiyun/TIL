package kr.ac.ajou.heidi.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_quiz.*
import org.jetbrains.anko.toast


class QuizActivity : AppCompatActivity() {

    private val questionBank = arrayOf(Question(R.string.question_africa, true),
            Question(R.string.question_americas, false),
            Question(R.string.question_asia, true),
            Question(R.string.question_mideast, false),
            Question(R.string.question_oceans, true)
    )

    private var currentIndex = 0
    private var previousIndex: MutableList<Int> = mutableListOf()


    companion object {
        const val KEY_INDEX = "index"
        const val REQUEST_CODE_CHEAT = 0
    }

    private var isCheater = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        savedInstanceState?.let {
            currentIndex = it.getInt(KEY_INDEX, 0)
            isCheater = it.getBoolean("isCheater")
        }


        Log.i("QuizActivity", "onCreate()")

        questionTextView.setOnClickListener {
            updateQuestion()
        }

        backButton.setOnClickListener {
            backPreviousQuestion()
        }

        nextButton.setOnClickListener {
            isCheater = false
            updateQuestion()
        }
        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        cheatButton.setOnClickListener {
            //            val intent = Intent(this, CheatActivity::class.java)
//            intent.putExtra("answer", questionBank[currentIndex].answerTrue)
//            startActivity(intent)
            val intent = newIntent(this, questionBank[currentIndex].answerTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        val question = questionBank[currentIndex].textResId
        questionTextView.text = getString(question)

    }

    private fun updateQuestion() {
        previousIndex.add(currentIndex)
        currentIndex = (currentIndex + 1) % questionBank.size
        val question = questionBank[currentIndex].textResId
        questionTextView.text = getString(question)
    }

    private fun backPreviousQuestion() {
        currentIndex = previousIndex.last()
        val question = questionBank[previousIndex.last()].textResId
        questionTextView.text = getString(question)
        previousIndex.removeAt(previousIndex.last())
    }

    private fun checkAnswer(userPressedTrue: Boolean) {
        val answerIsTrue = questionBank[currentIndex].answerTrue

        if (isCheater || questionBank[currentIndex].isCheater) {
            questionBank[currentIndex].isCheater = true
            toast(R.string.judgment_toast)
        } else {
            if (userPressedTrue == answerIsTrue) {
                toast(R.string.correct_toast)
            } else {
                toast(R.string.incorrect_toast)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("QuizActivity", "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.i("QuizActivity", "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.i("QuizActivity", "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.i("QuizActivity", "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("QuizActivity", "onDestroy()")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(KEY_INDEX, currentIndex)
        outState?.putBoolean("isCheater", isCheater)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == REQUEST_CODE_CHEAT) {
            data?.let {
                isCheater = wasAnswerShown(data)
            }
        }
    }
}

