package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        String result = getIntent().getStringExtra(QuizActivity.FINAL_RESULT);

        TextView finalScore_Text_View = findViewById(R.id.finalScorePoints);
        String message = "Your final score is " + result;
        finalScore_Text_View.setText(message);


        Button backButton = findViewById(R.id.restartButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(NextActivity.this, QuizActivity.class);
            startActivity(intent);
        });
    }
}