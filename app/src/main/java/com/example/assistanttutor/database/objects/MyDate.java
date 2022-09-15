package com.example.assistanttutor.database.objects;

public class MyDate {

    private int id;
    private String title, date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MyDate(int id, String title, String date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }
}
