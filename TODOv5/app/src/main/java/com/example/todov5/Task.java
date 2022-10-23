package com.example.todov5;

import java.util.Date;
import java.util.UUID;

public class Task {

    private UUID id;
    private String name;
    private Date date;
    private boolean done;

    public Task() {
        this.id = UUID.randomUUID();
        this.date = new Date();
        this.done = false;
    }

    public void setName(String toString) {
        this.name = toString;
    }

    public boolean isDone() {
        return this.done;
    }
    public void setDone(boolean isChecked) {
        this.done = true;
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
}
