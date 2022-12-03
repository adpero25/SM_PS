package com.example.booksearchapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class Book implements Serializable {

    @SerializedName("title")
    private String title;

    @SerializedName("author_name")
    private List<String> authors;

    @SerializedName("cover_i")
    private String cover;

    @SerializedName("number_of_pages_median")
    private String numberOfPages;

    @SerializedName("publish_date")
    private List<String> dateOfRelease;

    @SerializedName("language")
    private List<String> language;

    @SerializedName("subject")
    private List<String> subject;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public List<String> getDateOfRelease() {
        return dateOfRelease;
    }

    public void setDateOfRelease(List<String> dateOfRelease) {
        this.dateOfRelease = dateOfRelease;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public List<String> getSubject() {
        return subject;
    }

    public void setSubject(List<String> subject) {
        this.subject = subject;
    }
}
