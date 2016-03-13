package com.heinzchen.chihu;

import android.app.Application;
import android.content.Context;

import com.heinzchen.chihu.utils.MLog;

/**
 * Created by chen on 2016/3/3.
 */
public class CApplication extends Application {
    public static boolean DEBUG = true;
    public static final String TAG = CApplication.class.getSimpleName();

    public static Context GLOBAL_CONTEXT = null;

    @Override
    public void onCreate() {
        super.onCreate();
//        try {
//            Thread.sleep(5000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        GLOBAL_CONTEXT = this;

        MLog.i(TAG, "onCreate");
    }
}
