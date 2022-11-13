package com.example.todov5;

import java.util.Date;
import java.util.UUID;

public class Task {

    private UUID id;
    private String name;
    private Date date;
    private boolean done;
    private Category category;

    public Task() {
        this.id = UUID.randomUUID();
        this.date = new Date();
        this.done = false;
        this.category = Category.BRAK;
    }

    public void setName(String toString) {
        this.name = toString;
    }

    public boolean isDone() {
        return this.done;
    }
    public void setDone(boolean isChecked) {
        this.done = isChecked;
    }


    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date _date) {
        this.date = _date;
    }

    public void setCategory(Category _category) {
        this.category = _category;
    }

    public Category getCategory(){
        return this.category;
    }
}
