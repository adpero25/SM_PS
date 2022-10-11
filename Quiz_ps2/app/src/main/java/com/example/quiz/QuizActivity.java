package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private static final String CURRENT_INDEX_KEY = "currentIndex";
    private static final String USER_SCORE = "score";
    private static final String QUESTION_ARRAY = "questions";
    public static final String KEY_EXTRA_ANSWER = "correctAnswer";

    private TextView questionTextView;
    private ProgressBar progressBar;
    private int score;
    private int currentIndex;
    private final int numberOfQuestions = 5;
    int[] selectedQuestions;


    private final Question[] questions = new Question[] {
            new Question(R.string.q_activity, true),
            new Question(R.string.q_find_resources, false),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_version, false),
            new Question(R.string.q_pajak, true),
            new Question(R.string.q_australia, true),
            new Question(R.string.q_mastif, true),
            new Question(R.string.q_mysz, false),
            new Question(R.string.q_porzeczka, false),
            new Question(R.string.q_sily, false),
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Log.d("info_onCreate", "onCreate method has started");

        List<Integer> randomizer = new ArrayList<>();
        selectedQuestions = new int[numberOfQuestions];
        if(savedInstanceState != null)
        {
            score = savedInstanceState.getInt(USER_SCORE);
            currentIndex = savedInstanceState.getInt(CURRENT_INDEX_KEY);
            selectedQuestions = savedInstanceState.getIntArray(QUESTION_ARRAY);
        }
        else
        {
            score = 0;
            currentIndex = 0;

            /* Process of randomizing questions*/
            for(int i =0; i< questions.length; i++)
                randomizer.add(i);

            Collections.shuffle(randomizer);

            for (int i = 0; i < numberOfQuestions; i++)
                selectedQuestions[i] = randomizer.get(i);
            /*=================================*/

        }

        Button trueButton = findViewById(R.id.trueButton);
        Button falseButton = findViewById(R.id.falseButton);
        Button nextButton = findViewById(R.id.nextButton);
        Button promptButton = findViewById(R.id.promptButton);
        questionTextView = findViewById(R.id.question_Text_View);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);

        progressBar.setVisibility(View.VISIBLE);
        updateProgressBar();

        /* PROMPT BUTTON ONCLICK*/
        promptButton.setOnClickListener(view -> {
            Intent intent = new Intent(QuizActivity.this, PromptActivity.class);
            boolean correctAnswer = questions[currentIndex].isTrueAnswer();
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
            startActivity(intent);
        });

        /* TRUE BUTTON ONCLICK*/
        trueButton.setOnClickListener(v -> checkAnswerCorrectness(true));

        /* FALSE BUTTON ONCLICK*/
        falseButton.setOnClickListener(v -> checkAnswerCorrectness(false));

        /* NEXT BUTTON ONCLICK*/
        nextButton.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1);
            if(currentIndex == numberOfQuestions)
            {
                Intent intent = new Intent(QuizActivity.this, NextActivity.class);
                intent.putExtra("finalResult", String.valueOf(score));
                startActivity(intent);
            }
            else{
                setNextQuestion();
                updateProgressBar();
            }
        });
        setNextQuestion();
    }



    private void setNextQuestion() {
        questionTextView.setText(questions[selectedQuestions[currentIndex]].getQuestionId());
    }

    private void updateProgressBar() {
        progressBar.incrementProgressBy( (100 / numberOfQuestions) );
    }

    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[selectedQuestions[currentIndex]].isTrueAnswer();
        int resultMessageId;

        if(userAnswer == correctAnswer){
            resultMessageId = R.string.correctAnswer;
            score = score + 1;
        }
        else {
            resultMessageId = R.string.incorrectAnswer;
        }

        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart(); /* first line is always super() call*/
        Log.d("info_onStart", "onStart method has started");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("info_onResume", "onResume method has started");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("info_onPause", "onPause method has started");

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_INDEX_KEY, currentIndex);
        outState.putInt(USER_SCORE, score);
        outState.putIntArray(QUESTION_ARRAY, selectedQuestions);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("info_onStop", "onStop method has started");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("info_onDestroy", "onDestroy  method has started");

    }
}