package com.prateekgrover.redditline.repository.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class SharedPreferenceManager {

    private static final String SHARED_PREFERENCE_NAME = "RedditLineSharedPreference";
    private static SharedPreferenceManager instance;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private SharedPreferenceManager(Context context)  {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static synchronized SharedPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceManager(context);
        }

        return instance;
    }

    public int getInteger(@Keys String key, int defaultVal) {
        return mSharedPreferences.getInt(key, defaultVal);
    }

    public void putInteger(@Keys String key, int defaultVal) {
        mEditor.putInt(key, defaultVal).apply();
    }

    public boolean getBoolean(@Keys String key, boolean defaultVal) {
        return mSharedPreferences.getBoolean(key, defaultVal);
    }

    public void putBoolean(@Keys String key, boolean defaultVal) {
        mEditor.putBoolean(key, defaultVal).apply();
    }

    public void putObject(@Keys String key, Object value) {
        mEditor.putString(key, new Gson().toJson(value)).apply();
    }

    public <T> T getObject(@Keys String key, Class<T> typeOfObject) {
        String value = getString(key, null);
        if (value == null) {
            return null;
        }
        return new Gson().fromJson(value, typeOfObject);
    }

    public String getString(@Keys String key, String defaultVal) {
        return mSharedPreferences.getString(key, defaultVal);
    }

    public Object getObject(@Keys String key, Type type) {
        String value = getString(key, null);
        if (value == null) {
            return null;
        }
        return new Gson().fromJson(value, type);
    }

    public void putString(@Keys String key, String value) {
        mEditor.putString(key, value).apply();
    }

    public long getLong(@Keys String key, long defaultVal) {
        return mSharedPreferences.getLong(key, defaultVal);
    }

    public void putLong(@Keys String key, long value) {
        mEditor.putLong(key, value).apply();
    }

    public void reset() {
        mEditor.clear();
        mEditor.commit();
    }

    public @interface Keys {
        String AUTH = "auth";
    }
}
