package com.example.booksearchapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class BookDetailsActivity extends AppCompatActivity {

    private TextView bookTitleTextView;
    private TextView bookAuthorTextView;
    private TextView numberOfPagesTextView;
    private TextView dateOfReleaseTextView;
    private TextView subjectTextView;
    private TextView languageTextView;
    private ImageView bookCover;
    private Book book;
    private static final String IMAGE_URL_BASE = "https://covers.openlibrary.org/b/id/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        bookTitleTextView = findViewById(R.id.details_book_title);
        bookAuthorTextView = findViewById(R.id.details_book_author);
        numberOfPagesTextView = findViewById(R.id.details_number_of_pages);
        dateOfReleaseTextView = findViewById(R.id.details_date_of_release);
        subjectTextView = findViewById(R.id.details_subject);
        languageTextView = findViewById(R.id.details_language);
        bookCover = findViewById(R.id.details_img_cover);

        Intent intent = getIntent();

        book = (Book) intent.getSerializableExtra(MainActivity.BOOK_DETAILS);

        if(book != null && !checkNullOrEmpty( book.getTitle() ) && book.getAuthors() != null ) {
            /* Data loading */
            bookTitleTextView.setText(book.getTitle());
            numberOfPagesTextView.setText(book.getNumberOfPages());

            if(book.getAuthors() != null)
                bookAuthorTextView.setText( TextUtils.join(", ", book.getAuthors()) );
            else
                bookAuthorTextView.setText("UnKnown");

            if(book.getDateOfRelease() != null)
                dateOfReleaseTextView.setText( TextUtils.join(", ", book.getDateOfRelease()) );
            else
                dateOfReleaseTextView.setText("UnKnown");

            if(book.getSubject() != null)
                subjectTextView.setText( TextUtils.join(", ", book.getSubject()) );
            else
                subjectTextView.setText("UnKnown");

            if(book.getLanguage() != null)
                languageTextView.setText( TextUtils.join(", ", book.getLanguage()) );
            else
                languageTextView.setText("UnKnown");

            /* Image loading */
            if(book.getCover() != null) {
                Picasso.with(this)
                        .load(IMAGE_URL_BASE + book.getCover() + "-L.jpg")
                        .placeholder(R.drawable.ic_baseline_book_120).into(bookCover);
            }
            else {
                bookCover.setImageResource(R.drawable.ic_baseline_book_120);
            }
        }
    }

    private boolean checkNullOrEmpty(String title) {
        return title != null && TextUtils.isEmpty(title);

    }
}