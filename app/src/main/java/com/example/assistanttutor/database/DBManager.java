package com.example.assistanttutor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import com.example.assistanttutor.database.objects.Course;
import com.example.assistanttutor.database.objects.Lesson;

import java.util.concurrent.ExecutionException;

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
        database.execSQL("PRAGMA foreign_keys = ON");
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public long insertCourse(Course course){
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", course.getTitle());
            contentValues.put("description", course.getDescription());
            contentValues.put("planning", course.getPlanning());
            return database.insert("courses", null, contentValues);
        } catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return  -1;
        }
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
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", course.getTitle());
            contentValues.put("description", course.getDescription());
            contentValues.put("planning", course.getPlanning());
            return database.update("courses",  contentValues, "_id = ?", new String[]{courseId + ""});
        } catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return  -1;
        }
   }

    public long removeCourse(int courseId) {
        try{
            return database.delete("courses", "_id = ?", new String[]{courseId + ""});
        } catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return  -1;
        }
    }

    public long addDateToCourse(int courseId, String newDate, String theme) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("courseId", courseId);
            contentValues.put("date_", newDate);
            contentValues.put("theme", theme);
            return database.insert("planning", null, contentValues);
        } catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return  -1;
        }
    }

    public Cursor fetchDates(int courseId) {
        String [] columns = new String[] {"_id", "courseId", "theme", "date_"};
        Cursor cursor = database.query("planning", columns, "courseId = ?", new String[]{courseId+""}, null, null, "date_");
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public long removeDate(int id) {
        try{
            return database.delete("planning", "_id = ?", new String[]{id + ""});
        } catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    public int removeStudent(int id) {
        try{
            return database.delete("students", "_id = ?", new String[]{id + ""});
        } catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return -1;
        }
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
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", newStudentName);
            return (int) database.insert("students", null, contentValues);
        } catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    public Cursor fetchStudentsOnCourse(int courseId) {
        String [] columns = new String[] {"_id", "name"};
        Cursor cursor = database.query("records", columns, "courseId = ?", new String[]{courseId + ""}, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }


    public int removeStudentOnCourse(String studentName, int courseId) {
        try {
            return database.delete("records", "name = ? AND courseId = ?", new String[]{studentName, courseId + ""});
        } catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    public int insertStudentToCourse(String newStudentName, int courseId) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("courseId", courseId);
            contentValues.put("name", newStudentName);
            return (int) database.insert("records", null, contentValues);
        } catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    public Cursor fetchThemes(int courseId) {
        String [] columns = new String[] {"theme"};
        Cursor cursor = database.query("planning", columns, "courseId = ?", new String[]{courseId + ""}, "theme", null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int insertLesson(Lesson lesson) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("courseId", lesson.getCourseId());
            contentValues.put("name", lesson.getName());
            contentValues.put("theme", lesson.getTheme());
            contentValues.put("cost", lesson.getCost());
            contentValues.put("score", lesson.getScore());
            contentValues.put("date_", lesson.getDate());
            return (int) database.insert("lessons", null, contentValues);
        } catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    public Cursor fetchLessons() {
        String [] columns = new String[] {"courseId", "cost", "score", "name", "theme", "date_"};
        Cursor cursor = database.query("lessons", columns, null, null, null, null, "date_ DESC");
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
}
