package com.example.assistanttutor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context, "assistant_tutor.db", null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table courses (_id integer primary key autoincrement, title TEXT unique, description TEXT, planning TEXT)");
        db.execSQL("create table students (_id integer primary key autoincrement, name TEXT unique)");
        db.execSQL("create table lessons (_id integer primary key autoincrement, title TEXT, name Text, theme Text, paid boolean, score integer)");
        db.execSQL("create table records (_id integer primary key autoincrement, title TEXT, name Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists courses");
        db.execSQL("drop table if exists students");
        db.execSQL("drop table if exists lessons");
        db.execSQL("drop table if exists records");
        onCreate(db);
    }
}
