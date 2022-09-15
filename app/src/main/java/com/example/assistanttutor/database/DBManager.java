package com.example.assistanttutor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.assistanttutor.database.objects.Course;

public class DBManager {

    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;


    public DBManager(Context context) {
        this.context = context;
    }

    public DBManager open() throws SQLException{
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public long insertCourse(Course course){
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", course.getTitle());
        contentValues.put("description", course.getDescription());
        contentValues.put("planning", course.getPlanning());
        return database.insert("courses", null, contentValues);
    }

    public Cursor fetchCourses(){
        String [] columns = new String[] {"_id", "title", "description", "planning"};
        Cursor cursor = database.query("courses", columns, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public long updateCourse(Course course, int courseId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", course.getTitle());
        contentValues.put("description", course.getDescription());
        contentValues.put("planning", course.getPlanning());
        return database.update("courses",  contentValues, "_id = ?", new String[]{courseId + ""});
    }

    public long removeCourse(int courseId) {
        return database.delete("courses", "_id = ?", new String[]{courseId + ""});
    }

    public long addDateToCourse(String courseTitle, String newDate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", courseTitle);
        contentValues.put("date_", newDate);
        return database.insert("planning", null, contentValues);
    }

    public Cursor fetchDates() {
        String [] columns = new String[] {"_id", "title", "date_"};
        Cursor cursor = database.query("planning", columns, null, null, null, null, "date_");
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public long removeDate(int id) {
        return database.delete("planning", "_id = ?", new String[]{id + ""});
    }

    public int removeStudent(int id) {
        return database.delete("students", "_id = ?", new String[]{id + ""});
    }

    public Cursor fetchStudents() {
        String [] columns = new String[] {"_id", "name"};
        Cursor cursor = database.query("students", columns, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int insertStudent(String newStudentName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", newStudentName);
        return (int) database.insert("students", null, contentValues);
    }
}
