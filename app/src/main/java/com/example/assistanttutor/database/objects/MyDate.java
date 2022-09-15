package com.example.assistanttutor.database.objects;

public class MyDate {

    private int id, courseId;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MyDate(int id, int courseId, String date) {
        this.id = id;
        this.courseId = courseId;
        this.date = date;
    }


    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
