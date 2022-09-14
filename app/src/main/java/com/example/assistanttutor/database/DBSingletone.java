package com.example.assistanttutor.database;

import android.content.Context;

public class DBSingletone {

    private static DBSingletone dbSingletone;
    private final DBManager dbManager;
    private DBSingletone(Context context){
        dbManager = new DBManager(context);
        dbManager.open();
    }

    public static DBSingletone getInstance(Context context){
        if(dbSingletone == null){
            dbSingletone = new DBSingletone(context);
        }
        return dbSingletone;
    }



    public DBManager getDbManager(){
        return dbManager;
    }


}
