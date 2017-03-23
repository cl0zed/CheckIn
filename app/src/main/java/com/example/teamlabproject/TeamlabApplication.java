package com.example.teamlabproject;

import android.app.Application;

import com.example.teamlabproject.environment.TeamLabNotificationManager;
import com.example.teamlabproject.network.TeamRequestQueue;
import com.example.teamlabproject.utils.UserSettings;
import com.google.common.eventbus.EventBus;

public class TeamlabApplication extends Application {

    private static TeamlabApplication mInstance;

    private TeamRequestQueue mRequestQueue;
    private UserSettings mUserSettings;
    private EventBus mEventBus;
    private TeamLabNotificationManager mNotificationManager;


    public TeamlabApplication(){
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mRequestQueue = new TeamRequestQueue(this);
        mUserSettings = new UserSettings(this);
        mEventBus = new EventBus("team");
        mNotificationManager = new TeamLabNotificationManager();
    }

    public static TeamlabApplication getInstance() {
        return mInstance;
    }

    public TeamRequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public UserSettings getUserSettings(){
        return mUserSettings;
    }

    public EventBus getEventBus(){
        return mEventBus;
    }

    public TeamLabNotificationManager getNotificationManager(){
        return mNotificationManager;
    }
}
