package com.example.libraryappv3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryappv3.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final int NEW_BOOK_ACTIVITY_RQ = 1;
    public static final int EDIT_BOOK_ACTIVITY_RQ = 2;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private BookViewModel bookViewModel;
    private FloatingActionButton addBookButton;
    private int ReqCode;
    private int EditReqCode;
    public static String BOOK_TO_EDIT = "BOOK_TO_EDIT";
    public static String EDIT_BOOK_AUTHOR = "EDIT_BOOK_AUTHOR";
    public static String EDIT_BOOK_TITLE = "EDIT_BOOK_TITLE";
    public static String BOOK_ID = "BOOK_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        bookViewModel.findAll().observe(this, adapter::setBooks);

        addBookButton = findViewById(R.id.add_button);
        addBookButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditBookActivity.class);

            openActivityForResultLauncher.launch(intent);
        });

    }

    ActivityResultLauncher<Intent> openActivityForResultLauncher = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() != Activity.RESULT_OK) { return; }
                    if(result.getData() == null) { return; }
                    ReqCode = result.getData().getIntExtra(NEW_BOOK_ACTIVITY_RQ + "", 1);

                    String title = result.getData().getStringExtra(EditBookActivity.EDIT_TITLE);
                    String author = result.getData().getStringExtra(EditBookActivity.EDIT_AUTHOR);

                    Book book = new Book(title, author);

                    bookViewModel.insert(book);

                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_added), Snackbar.LENGTH_LONG).show();
                }
            });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }



    private class BookHolder extends RecyclerView.ViewHolder {

        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;
        Book book;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.book_list_item, parent, false));

            bookTitleTextView = itemView.findViewById(R.id.title_label);
            bookAuthorTextView = itemView.findViewById(R.id.author_label);

            itemView.setOnLongClickListener(view -> {
                int p=getLayoutPosition();

                bookViewModel.delete(book);
                return true;
            });


            itemView.setOnClickListener(view -> {
                int p=getLayoutPosition();

                Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
                intent.putExtra(EDIT_BOOK_TITLE, book.getTitle());
                intent.putExtra(EDIT_BOOK_AUTHOR, book.getAuthor());
                intent.putExtra(BOOK_ID, book.getId());
                openEditActivityForResultLauncher.launch(intent);
            });
        }


        public void bind(Book _book) {
            this.book = _book;
            bookTitleTextView.setText(book.getTitle());
            bookAuthorTextView.setText(book.getAuthor());
        }
    }

    ActivityResultLauncher<Intent> openEditActivityForResultLauncher = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() != Activity.RESULT_OK) { return; }
                    if(result.getData() == null) { return; }
                    ReqCode = result.getData().getIntExtra(EDIT_BOOK_ACTIVITY_RQ + "", 2);

                    String title = result.getData().getStringExtra(EditBookActivity.EDIT_TITLE);
                    String author = result.getData().getStringExtra(EditBookActivity.EDIT_AUTHOR);
                    int id = result.getData().getIntExtra(BOOK_ID, 0);

                    Book book = new Book(id, title, author);

                    bookViewModel.update(book);

                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_edited), Snackbar.LENGTH_LONG).show();
                }
            });



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
                Log.d("MainActivity", "No books");
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) {
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.empty_not_saved), Snackbar.LENGTH_LONG).show();
        }
    }
}