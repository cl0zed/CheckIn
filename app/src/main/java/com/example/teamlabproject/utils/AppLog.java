package com.example.teamlabproject.utils;


import android.util.Log;

import com.example.teamlabproject.TeamlabApplication;

public class AppLog{

    public static String TAG = TeamlabApplication.class.getName();

    public static void d(String debugLog){
        Log.d(TAG, debugLog);
    }

    public static void e(String errorLog){
        Log.e(TAG, errorLog);
    }
}
