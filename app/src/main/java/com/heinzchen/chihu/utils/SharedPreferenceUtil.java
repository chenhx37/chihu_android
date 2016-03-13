package com.heinzchen.chihu.utils;

import android.content.SharedPreferences;
import android.preference.SwitchPreference;

import com.heinzchen.chihu.CApplication;

/**
 * Created by chen on 2016/3/13.
 */
public class SharedPreferenceUtil {

    private static SharedPreferenceUtil mInstance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private SharedPreferenceUtil() {
        mSharedPreferences = CApplication.GLOBAL_CONTEXT.getSharedPreferences("Chihu", 0);
    }

    public static SharedPreferenceUtil getInstance() {
        if (null == mInstance) {
            synchronized (SharedPreferenceUtil.class) {
                if (null == mInstance) {
                    mInstance = new SharedPreferenceUtil();
                }
            }
        }
        return mInstance;
    }

    public String getString(String name, String defaultValue) {
        return mSharedPreferences.getString(name, defaultValue);
    }

    public void setString(String name, String value) {
        if (null == mEditor) {
            mEditor = mSharedPreferences.edit();
        }
        mEditor.putString(name, value);
        mEditor.apply();

    }
}
