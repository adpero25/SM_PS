package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private TextView questionTextView;
    private TextView scoreTextView;
    private ProgressBar progressBar;
    private int score;
    private int currentIndex;
    private ArrayList<Integer> randomizer;
    private int numberOfQuestions = 5;
    int[] selectedQuestions;


    private Question[] questions = new Question[] {
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

        randomizer = new ArrayList<Integer>();
        selectedQuestions = new int[numberOfQuestions];
        score = 0;
        currentIndex = 0;
        trueButton = findViewById(R.id.trueButton);
        falseButton = findViewById(R.id.falseButton);
        nextButton = findViewById(R.id.nextButton);
        questionTextView = findViewById(R.id.question_Text_View);
        scoreTextView = findViewById(R.id.score_Text_View);
        progressBar = findViewById(R.id.progressBar);

        /* Process of randomizing questions*/
        for(int i =0; i< questions.length; i++)
            randomizer.add(i);

        Collections.shuffle(randomizer);

        for (int i = 0; i < numberOfQuestions; i++)
            selectedQuestions[i] = randomizer.get(i);
        /*=================================*/


        progressBar.setVisibility(View.VISIBLE);
        updateProgressBar();

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkAnswerCorrectness(true);
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void updateScorePoints() {
        scoreTextView.setText("Your score is " + score);
    }

    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[selectedQuestions[currentIndex]].isTrueAnswer();
        int resultMessageId = 0;

        if(userAnswer == correctAnswer){
            resultMessageId = R.string.correctAnswer;
            score = score + 1;
        }
        else {
            resultMessageId = R.string.incorrectAnswer;
        }

        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
        updateScorePoints();
    }
}