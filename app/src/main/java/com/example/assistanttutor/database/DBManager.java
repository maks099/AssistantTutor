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
        System.out.println(courseId + " ****************************");
        return database.delete("courses", "_id = ?", new String[]{courseId + ""});
    }
}
