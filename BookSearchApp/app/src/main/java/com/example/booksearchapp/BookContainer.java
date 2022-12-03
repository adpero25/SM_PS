package com.example.booksearchapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookContainer {

    @SerializedName("docs")
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
