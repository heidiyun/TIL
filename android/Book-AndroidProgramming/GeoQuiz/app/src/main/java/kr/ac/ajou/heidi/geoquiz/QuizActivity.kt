package kr.ac.ajou.heidi.geoquiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
    private var previousIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        updateQuestion()

        questionTextView.setOnClickListener {
            previousIndex = currentIndex
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        backButton.setOnClickListener {
            backPreviousQuestion()
        }

        nextButton.setOnClickListener {
            previousIndex = currentIndex
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

    }

    private fun updateQuestion() {
        val question = questionBank[currentIndex].textResId
        questionTextView.text = getString(question)
    }

    private fun backPreviousQuestion() {
        val question = questionBank[previousIndex].textResId
        questionTextView.text = getString(question)
    }

    private fun checkAnswer(userPressedTrue: Boolean) {
        val answerIsTure = questionBank[currentIndex].answerTrue

        if (userPressedTrue == answerIsTure) {
            toast(R.string.correct_toast)
        } else {
            toast(R.string.incorrect_toast)
        }

    }
}

