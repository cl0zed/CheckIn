package com.example.teamlabproject.utils;

import android.os.Handler;

import com.example.teamlabproject.TeamlabApplication;

public class Events {

    private static Handler mHandler = null;

    public static void postOnUI(final Object object){
        if (mHandler == null){
            mHandler = new Handler();
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TeamlabApplication.getInstance().getEventBus().post(object);
            }
        });
    }

    public static void registerListener(Object listener){
        TeamlabApplication.getInstance().getEventBus().register(listener);
    }

    public static void unregisterListener(Object listener){
        TeamlabApplication.getInstance().getEventBus().unregister(listener);
    }
}
