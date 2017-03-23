package com.example.teamlabproject.utils;

import android.content.Context;

import com.lid.android.commons.settings.PreferenceSettings;

public class UserSettings extends PreferenceSettings {

    private static final String TOKEN = "token";
    private static final String THEME_TITLE = "theme_title";
    private static final String THEME_ID = "theme_id";
    private static final String GENDER = "sex";
    private static final String LAST_WIFI_NAME = "last_wifi_name";


    public UserSettings(Context context){
        super(context);
    }

    public void setGender(Gender gender){
        set(GENDER, gender.toString());
    }

    public Gender getGender(){
        return Gender.fromString(getString(GENDER));
    }

    public void setToken(String newToken){
        set(TOKEN, newToken);
    }

    public String getToken(){
        return getString(TOKEN, "");
    }

    public String getTitle(){
        return getString(THEME_TITLE, "");
    }

    public void setTitle(String title){
        set(THEME_TITLE, title);
    }

    public void setId(int id){
        set(THEME_ID, id);
    }

    public int getId(){
        return getInt(THEME_ID, 0);
    }

    public void setLastWifiName(String name){
	    set(LAST_WIFI_NAME, name);
    }

	public String getLastWifiName(){
		return getString(LAST_WIFI_NAME);
	}

}
