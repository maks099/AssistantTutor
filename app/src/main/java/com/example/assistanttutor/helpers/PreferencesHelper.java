package com.example.assistanttutor.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {
    public static void save(Activity activity, String key, String value){
        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(key, value).apply();
    }

    public static String get(Activity activity, String key){
        return activity.getPreferences(Context.MODE_PRIVATE).getString(key, "");
    }
}
