package kr.ac.ajou.heidi.quizapp;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mBackButton;
    private TextView mQuestionView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;
    private List<Integer> mPreviousIndex = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mQuestionView = findViewById(R.id.question_text_view);

        updateQuestion();

        mQuestionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuestion();
            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });



        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuestion();
            }
        });

        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPreviousQuestion();
            }
        });

    }

    private void updateQuestion() {
        mPreviousIndex.add(mCurrentIndex);
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        int question = mQuestionBank[mCurrentIndex].getmTextResId();
        mQuestionView.setText(question);
    }

    private void backPreviousQuestion() {
        mCurrentIndex = mPreviousIndex.get(mPreviousIndex.size() - 1);
        int question = mQuestionBank[mPreviousIndex.get(mPreviousIndex.size() - 1)].getmTextResId();
        mQuestionView.setText(question);
        mPreviousIndex.remove(mPreviousIndex.size() - 1);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}