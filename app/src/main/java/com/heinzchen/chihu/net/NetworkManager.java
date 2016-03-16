package com.heinzchen.chihu.net;

import android.content.SharedPreferences;

import com.heinzchen.chihu.CApplication;
import com.heinzchen.chihu.utils.MLog;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chen on 2016/3/4.
 */
public class NetworkManager {
    public static final String TAG = NetworkManager.class.getSimpleName();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String COOKIE = "cookie";

    private static NetworkManager mInstance;
    private OkHttpClient mClient;

    private NetworkManager() {
        mClient = new OkHttpClient();
    }

    public static NetworkManager getInstance() {
        if (null == mInstance) {
            synchronized (NetworkManager.class) {
                if (null == mInstance) {
                    mInstance = new NetworkManager();
                }
            }
        }
        return mInstance;
    }

    public void sendRequest(final Request request,final INetCallbackListener listener) {
        MLog.i(TAG, "sendRequest");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null == mClient) {
                    mClient = new OkHttpClient();
                }
                try {
                    Response response = mClient.newCall(request).execute();
                    MLog.i(TAG, "request sent");
                    saveCookie(response.header("Set-Cookie"));

                    listener.onResponse(response.body().string());


//                    MLog.i(TAG, response.body().string());
//                    EventBus.getDefault().post(msg);
                } catch (IOException e) {
                    MLog.e(TAG, e.getMessage());
                }
            }
        }).start();
    }

    private void saveCookie(String cookie) {
        MLog.i(TAG, "cookie=" + cookie);
        if (null == cookie) {
            return;
        }

        SharedPreferences sp = CApplication.GLOBAL_CONTEXT.getSharedPreferences("Chihu", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(COOKIE, cookie);
        editor.apply();
    }
//    @Deprecated
//    public void requestRegisterUser(final Chihu.UserAccount userAccount) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String url = InterfaceDef.CHIHU_SERVER+InterfaceDef.REGISTER_USER;
//                byte[] byteArray = userAccount.toByteArray();
//
//                OkHttpClient client = new OkHttpClient();
//                RequestBody body = RequestBody.create(JSON, byteArray);
//                MLog.i(TAG, "the body is " + body.toString());
//
//                Request request = new Request.Builder()
//                        .url(url)
//                        .post(body)
//                        .build();
//
//                Request getRequest = new Request.Builder()
//                        .url(url)
//                        .build();
//
//
//                try {
//                    Response getResponse = client.newCall(getRequest).execute();
//
//                    MLog.i(TAG, "going to request");
//                    Response response = client.newCall(request).execute();
//                    MLog.i(TAG, "request Done");
//                    MLog.i(TAG, response.body().string());
//                } catch (IOException e) {
//                    MLog.e(TAG, e.getMessage());
//                }
//
//            }
//        }).start();
//
//
//    }
}
