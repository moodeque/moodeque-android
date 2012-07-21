package com.whiterabbit.hackitaly.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesStore {
	
	
	public static final String PREF_NAME = "com.whiterabbit.hackitaly";
	public static final String USERNAME = "Count";
    public static final String CURRENTLY_PLAYING = "Count";
	
	public static String getUsername(Context c)
	{
		int mode = Activity.MODE_PRIVATE;
		SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);		
		return mySharedPreferences.getString(USERNAME, "");
	}


    public static void setUserName(Context c, String name){
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(USERNAME, name);
        editor.commit();
    }


    public static String getCurrentlyPlaying(Context c)
    {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);
        return mySharedPreferences.getString(CURRENTLY_PLAYING, "");
    }


    public static void setCurrentlyPlaying(Context c, String playing){
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences mySharedPreferences = c.getSharedPreferences(PREF_NAME, mode);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(CURRENTLY_PLAYING, playing);
        editor.commit();
    }


	
}
