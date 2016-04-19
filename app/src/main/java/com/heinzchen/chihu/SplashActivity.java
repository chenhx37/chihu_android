package com.heinzchen.chihu;

import android.app.Activity;
import android.os.Bundle;

import com.heinzchen.chihu.account.ProfileManager;
import com.heinzchen.chihu.login.LoginActivity;
import com.heinzchen.chihu.customer.main.MainActivity;
import com.heinzchen.chihu.provider.ProviderMainActivity;
import com.heinzchen.chihu.utils.MLog;

import protocol.Chihu;

/**
 * Created by chen on 2016/4/13.
 */
public class SplashActivity extends Activity {
    public static final String TAG = SplashActivity.class.getSimpleName();
    private static final int PERIOD = 500;//ms to sleep
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        jump();
    }

    private void jump() {
        try {
            Thread.sleep(PERIOD);
        } catch (InterruptedException e) {
            MLog.e(TAG, e.getMessage());
        }

        if (ProfileManager.getInstance().getCookie() != null) {
            //already logged in
            Chihu.Profile profile = ProfileManager.getInstance().getCachedProfile();
            if (profile.getType() == Chihu.UserType.PROVIDER) {
                ProviderMainActivity.jumpToMe(this);
            } else {
                MainActivity.jumpToMe(this);
            }
        } else {
            LoginActivity.jumpToMe(this);
        }
        finish();
    }
}
