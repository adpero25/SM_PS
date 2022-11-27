package com.example.libraryappv3;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("DELETE FROM book")
    void deleteAll();

    @Query("SELECT * FROM book ORDER BY title")
    LiveData<List<Book>> findAll();

    @Query("SELECT * FROM book WHERE title LIKE :title")
    List<Book> findBookWithTitle(String title);

}
