package com.example.libraryappv3;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {
    private static final int NEW_BOOK_ACTIVITY_RQ = 1;

    private static BookDatabase databaseInstance;
    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract BookDao bookDao();

    static BookDatabase getDatabase(final Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                               BookDatabase.class, "book_database")
                               .addCallback(roomDatabaseCallback)
                               .build();
        }
        return databaseInstance;
    }

    private static final Callback roomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                BookDao dao = databaseInstance.bookDao();
                Book book = new Book("Clean code", "Robert C. Martin");
                dao.insert(book);
            });
        }
    };

}
