package kr.ac.ajou.heidi.geoquiz

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        if(savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
        }


        Log.i("QuizActivity", "onCreate()")

        questionTextView.setOnClickListener {
            updateQuestion()
        }

        backButton.setOnClickListener {
            backPreviousQuestion()
        }

        nextButton.setOnClickListener {
            updateQuestion()
        }
        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
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

        if (userPressedTrue == answerIsTrue) {
            toast(R.string.correct_toast)
        } else {
            toast(R.string.incorrect_toast)
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
        Log.i("QuizActivity", "onSaveInstanceState")
        outState?.putInt(KEY_INDEX, currentIndex)
    }
}

