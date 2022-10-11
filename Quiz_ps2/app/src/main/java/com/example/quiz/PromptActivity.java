package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PromptActivity extends AppCompatActivity {

    private boolean correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        TextView promptTextView = findViewById(R.id.prompt_Text_View);
        Button showAnswerButton = findViewById(R.id.yesButton);
        Button backButton = findViewById(R.id.noButton);

        /* SHOW ANSWER BUTTON*/
        showAnswerButton.setOnClickListener(view -> {
            correctAnswer = getIntent().getBooleanExtra(QuizActivity.KEY_EXTRA_ANSWER, true);
            int answer = correctAnswer ? R.string.trueButton : R.string.falseButton;
            promptTextView.setText(answer);
            backButton.setText(R.string.backButton);
        });

        /* BACK BUTTON*/
        backButton.setOnClickListener(view -> onBackPressed());

    }
}