package com.example.libraryappv3;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

@Entity(tableName="book")
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String author;

    public Book(String title, String author) {
        setAuthor(author);
        setTitle(title);
    }

    @Ignore
    public Book(int id, String title, String author) {
        setId(id);
        setAuthor(author);
        setTitle(title);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
