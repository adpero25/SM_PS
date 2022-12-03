package com.example.booksearchapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String BOOK_DETAILS = "BOOK_DETAILS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchBooksData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.menu_item_clear:
                clearList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchBooksData(String query) {

        String finalQuery = prepareQuery(query);
        BookService bookService = RetrofitInstance.getRertrofitInstance().create(BookService.class);

        Call<BookContainer> booksApiCall = bookService.findBooks(finalQuery);

        booksApiCall.enqueue(new Callback<BookContainer>() {
            @Override
            public void onResponse(@NonNull Call<BookContainer> call, @NonNull Response<BookContainer> response) {
                if (response.body() != null) {
                    setupBooksListView(response.body().getBooks());
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookContainer> call, @NonNull Throwable t) {
                Snackbar.make(findViewById(R.id.main_view), "Something went wrong! Try again later.",
                        BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }

    private class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String IMAGE_URL_BASE = "https://covers.openlibrary.org/b/id/";

        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;
        private TextView numberOfPagesTextView;
        private ImageView bookCover;
        private Book _book;
        private TableRow item;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.book_list_item, parent, false));

            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
            numberOfPagesTextView = itemView.findViewById(R.id.number_of_pages);
            bookCover = itemView.findViewById(R.id.img_cover);
            item = itemView.findViewById(R.id.item);

            item.setOnClickListener(this);

        }

    /*  Wykorzystana jest tu biblioteka Picasso do automatycznego ładowania obrazków.
        Korzystamy tu bezpośrednio z połączenia z API (z pominięciem biblioteki Retrofit).
        Podając URL do połączenia dodajemy sufiks definiujący wielkość obrazka (rozmiary: S, M, L) */

        public void bind(Book book) {
            this._book = book;
            if(book != null && !checkNullOrEmpty( book.getTitle() ) && book.getAuthors() != null ) {
                /* Data loading */
                bookTitleTextView.setText(book.getTitle());
                bookAuthorTextView.setText( TextUtils.join(", ", book.getAuthors()) );
                numberOfPagesTextView.setText(book.getNumberOfPages());

                /* Image loading */
                if(book.getCover() != null) {
                    Picasso.with(itemView.getContext())
                            .load(IMAGE_URL_BASE + book.getCover() + "-S.jpg")
                            .placeholder(R.drawable.ic_baseline_book_24).into(bookCover);
                }
                else {
                    bookCover.setImageResource(R.drawable.ic_baseline_book_24);
                }

            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), BookDetailsActivity.class);
            intent.putExtra(BOOK_DETAILS, _book);
            startActivity(intent);
        }
    }


    private class BookAdapter extends RecyclerView.Adapter<BookHolder> {

        private List<Book> books;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if(books != null) {
                Book book = books.get(position);
                holder.bind(book);
            }
            else {
                Log.d("Main Activity", "No books");
            }
        }

        @Override
        public int getItemCount() {
            if(books != null)
                return books.size();
            else
                return 0;
        }

        void setBooks(List<Book> books) {
            this.books = books;
            notifyDataSetChanged();
        }
    }

    private String prepareQuery(String query) {

        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);

    }

    private void setupBooksListView(List<Book> books) {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final BookAdapter adapter = new BookAdapter();

        adapter.setBooks(books);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private boolean checkNullOrEmpty(String title) {
        return title != null && TextUtils.isEmpty(title);

    }

    private void clearList() {
        setupBooksListView(null);

    }
}