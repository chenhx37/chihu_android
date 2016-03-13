package com.heinzchen.chihu.utils;

import android.util.Log;

import com.heinzchen.chihu.CApplication;

/**
 * Created by chen on 2016/3/3.
 * 加了debug开关
 */
public class MLog {
    public static void i(String TAG, String msg) {
        if (CApplication.DEBUG) {
            Log.i(TAG, msg);
        }

    }
    public static void e(String TAG, String msg) {
        if (CApplication.DEBUG) {
            Log.e(TAG, msg);
        }
    }
    public static void d(String TAG, String msg) {
        if (CApplication.DEBUG) {
            Log.d(TAG, msg);
        }
    }
    public static void v(String TAG, String msg) {
        if (CApplication.DEBUG) {
            Log.v(TAG, msg);
        }

    }
    public static void w(String TAG, String msg) {
        if (CApplication.DEBUG) {
            Log.w(TAG, msg);
        }

    }
}
