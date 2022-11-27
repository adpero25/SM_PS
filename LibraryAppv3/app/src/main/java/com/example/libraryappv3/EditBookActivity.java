package com.example.libraryappv3;

import static com.example.libraryappv3.MainActivity.BOOK_ID;
import static com.example.libraryappv3.MainActivity.BOOK_TO_EDIT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class EditBookActivity extends AppCompatActivity {
    private static final int NEW_BOOK_ACTIVITY_RQ = 1;

    public static final String EDIT_TITLE = "Edit Title";
    public static final String EDIT_AUTHOR = "Edit Author";

    private EditText editTitleEditText;
    private EditText editAuthorEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        editTitleEditText = findViewById(R.id.edit_book_title);
        editAuthorEditText = findViewById(R.id.edit_book_author);
        final Button button = findViewById(R.id.button_save);

        Intent i = getIntent();
        if(i.hasExtra(MainActivity.EDIT_BOOK_TITLE)) {
            /* EDITING BOOK */
            editTitleEditText.setText(i.getStringExtra(MainActivity.EDIT_BOOK_TITLE));
            editAuthorEditText.setText(i.getStringExtra(MainActivity.EDIT_BOOK_AUTHOR));

            button.setOnClickListener(v -> {

                Intent replyIntent = new Intent();

                if(TextUtils.isEmpty(editTitleEditText.getText()) || TextUtils.isEmpty(editAuthorEditText.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                }
                else {
                    String title = editTitleEditText.getText().toString();
                    replyIntent.putExtra(EDIT_TITLE, title);

                    String author = editAuthorEditText.getText().toString();
                    replyIntent.putExtra(EDIT_AUTHOR, author);

                    replyIntent.putExtra(BOOK_ID, i.getIntExtra(BOOK_ID, 0));

                    setResult(RESULT_OK, replyIntent);
                }

                finish();
            });
        }
        else{
            /* INSERTING NEW BOOK */

            button.setOnClickListener(v -> {

                Intent replyIntent = new Intent();

                if(TextUtils.isEmpty(editTitleEditText.getText()) || TextUtils.isEmpty(editAuthorEditText.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                }
                else {
                    String title = editTitleEditText.getText().toString();
                    replyIntent.putExtra(EDIT_TITLE, title);

                    String author = editAuthorEditText.getText().toString();
                    replyIntent.putExtra(EDIT_AUTHOR, author);

                    setResult(RESULT_OK, replyIntent);
                }

                finish();
            });
        }
    }
}