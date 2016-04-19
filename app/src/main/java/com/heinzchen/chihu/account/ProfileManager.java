package com.heinzchen.chihu.account;

import com.google.protobuf.InvalidProtocolBufferException;
import com.heinzchen.chihu.net.NetworkManager;
import com.heinzchen.chihu.utils.MLog;
import com.heinzchen.chihu.utils.SharedPreferenceUtil;

import protocol.Chihu;

/**
 * Created by chen on 2016/4/7.
 */
public class ProfileManager {
    public static final String TAG = ProfileManager.class.getSimpleName();
    public static final String PROFILE_KEY = "profile";
    private static ProfileManager mInstance;
    private ProfileManager() {}

    public static ProfileManager getInstance() {
        if (null == mInstance) {
            synchronized (ProfileManager.class) {
                if (null == mInstance) {
                    mInstance = new ProfileManager();
                }
            }
        }
        return mInstance;
    }

    public void cacheProfile(Chihu.Profile profile) {
        String pbString = new String(profile.toByteArray());
        SharedPreferenceUtil.getInstance().setString(PROFILE_KEY, pbString);
    }

    public Chihu.Profile getCachedProfile() {
        String str = SharedPreferenceUtil.getInstance().getString(PROFILE_KEY, null);
        if (null == str) {
            return null;
        }

        byte[] byteArray = str.getBytes();
        Chihu.Profile profile = null;
        try {
            profile = Chihu.Profile.parseFrom(byteArray);
        } catch (InvalidProtocolBufferException e) {
            MLog.e(TAG, e.getMessage());
        }
        return profile;
    }

    public String getCookie() {
        return SharedPreferenceUtil.getInstance().getString(NetworkManager.COOKIE, null);
    }

    public void setCookie(String cookie) {
        SharedPreferenceUtil.getInstance().setString(NetworkManager.COOKIE, cookie);
    }

    public void logout() {
        SharedPreferenceUtil.getInstance().setString(NetworkManager.COOKIE, null);
        SharedPreferenceUtil.getInstance().setString(PROFILE_KEY, null);
    }
}
