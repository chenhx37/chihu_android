package com.heinzchen.chihu.customer.main;

import com.google.protobuf.InvalidProtocolBufferException;
import com.heinzchen.chihu.account.ProfileManager;
import com.heinzchen.chihu.net.INetCallbackListener;
import com.heinzchen.chihu.net.NetworkManager;
import com.heinzchen.chihu.net.ProtocolManager;
import com.heinzchen.chihu.utils.EventBusMessage;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Request;
import protocol.Chihu;

/**
 * Created by chen on 2016/4/7.
 */
public class ProfilePresenter {
    public static final String TAG = ProfilePresenter.class.getSimpleName();

    private static ProfilePresenter mInstance;

    private ProfilePresenter() {}

    public static ProfilePresenter getInstance() {
        if (null == mInstance) {
            synchronized (ProfilePresenter.class) {
                if (null == mInstance) {
                    mInstance = new ProfilePresenter();
                }
            }
        }
        return mInstance;
    }

    public void getProfileFromServer() {
        MLog.i(TAG, "getProfileFromServer()");
        Request request = ProtocolManager.getProfile();
        NetworkManager.getInstance().sendRequest(request, new INetCallbackListener() {
            @Override
            public void onResponse(String pbString) {
                MLog.i(TAG, "onResponse,getProfileFromServer");
                Chihu.Profile profile = null;
                System.out.println(pbString.getBytes().length);
                try {
                    profile = Chihu.Profile.parseFrom(pbString.getBytes());
                } catch (InvalidProtocolBufferException e) {
                    MLog.e(TAG, e.getMessage());
                }
                if (null != profile) {
                    ProfileManager.getInstance().cacheProfile(profile);
                    GetProfileMessage msg = new GetProfileMessage();
                    msg.profile = profile;
                    EventBus.getDefault().post(msg);
                }
            }
        });
    }

    public void updateProfile(String username, String email, String netId, String phone, String receiver, String address) {
        Request request = ProtocolManager.updateProfile(username, email, netId, phone, receiver, address);
        NetworkManager.getInstance().sendRequest(request, new INetCallbackListener() {
            @Override
            public void onResponse(String pbString) {
                MLog.i(TAG, "onResponse,updateProfile");
                Chihu.Response resp = null;
                try {
                    resp = Chihu.Response.parseFrom(pbString.getBytes());
                } catch (InvalidProtocolBufferException e) {
                    MLog.e(TAG, e.getMessage());
                }

                if (null != resp && resp.getStatus().getNumber() == Chihu.Status.succeed_VALUE) {
                    UpdateProfileResultMessage msg = new UpdateProfileResultMessage();
                    msg.status = EventBusMessage.SUCCEED;
                    EventBus.getDefault().post(msg);
                } else {
                    UpdateProfileResultMessage msg = new UpdateProfileResultMessage();
                    msg.status = EventBusMessage.FAIL;
                    EventBus.getDefault().post(msg);
                }
            }
        });

    }
}
