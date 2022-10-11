package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    private TextView finalScore_Text_View;
    private Button backButton;
    private Button leaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        String result = getIntent().getStringExtra("finalResult");

        finalScore_Text_View = findViewById(R.id.finalScorePoints);
        finalScore_Text_View.setText("Your final score is " + result);


        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NextActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
    }
}