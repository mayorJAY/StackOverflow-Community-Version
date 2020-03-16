package com.example.josycom.flowoverstack.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    private static PreferenceHelper sPreferenceHelper;
    private SharedPreferences mSharedPreferences;
    private static String fileName = "SOCPreference";
    private SharedPreferences.Editor mEditor;

    public PreferenceHelper(Context context){
        mSharedPreferences = context.getApplicationContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public static PreferenceHelper getInstance(Context context){
        if (sPreferenceHelper == null)
            sPreferenceHelper = new PreferenceHelper(context);
        return sPreferenceHelper;
    }

    public void putString(String key, String value){
        mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public String getString(String key){
        return mSharedPreferences.getString(key, null);
    }

    public void putInt(String key, int value){
        mEditor = mSharedPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public int getInt(String key){
        return mSharedPreferences.getInt(key, 0);
    }

    public void clear(){
        mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
    }

    public void remove(){
        mEditor = mSharedPreferences.edit();
        mEditor.remove(fileName);
        mEditor.apply();
    }
}
