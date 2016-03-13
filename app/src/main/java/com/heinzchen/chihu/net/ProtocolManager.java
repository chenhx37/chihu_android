package com.heinzchen.chihu.net;

import com.heinzchen.chihu.CApplication;
import com.heinzchen.chihu.protocol.Chihu;
import com.heinzchen.chihu.utils.MLog;
import com.heinzchen.chihu.utils.SharedPreferenceUtil;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by chen on 2016/3/10.
 */
public class ProtocolManager {
    public static final String TAG = ProtocolManager.class.getSimpleName();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String SET_COOKIE_HEADER = "Cookie";

    private static ProtocolManager mInstance;

    private ProtocolManager() {}

    public static ProtocolManager getInstance() {
        if (null == mInstance) {
            synchronized (ProtocolManager.class) {
                if (null == mInstance) {
                    mInstance = new ProtocolManager();
                }
            }
        }
        return mInstance;
    }

    public static Request getRegisterUserRequest(String username, String password, String email, String school) {

        String url;
        if (CApplication.DEBUG) {
            url = InterfaceDef.CHIHU_DEBUG_SERVER.concat(InterfaceDef.REGISTER_USER);
        } else {
            url = InterfaceDef.CHIHU_SERVER.concat(InterfaceDef.REGISTER_USER);
        }


        Chihu.UserAccount.Builder builder = Chihu.UserAccount.newBuilder();
        builder.setUsername(username);
        builder.setPassword(password);
        builder.setEmail(email);
        builder.setSchool(Chihu.School.valueOf(Chihu.School.Sun_Yat_San_University_VALUE));

        Chihu.UserAccount userAccount = builder.build();

        RequestBody body = RequestBody.create(JSON, userAccount.toByteArray());
        MLog.i(TAG, userAccount.toByteArray().toString());
        System.out.print(userAccount.toByteArray().toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return request;
    }

    public static Request getLoginRequest(String username, String password) {
        String url;
        if (CApplication.DEBUG) {
            url = InterfaceDef.CHIHU_DEBUG_SERVER.concat(InterfaceDef.LOGIN);
        } else {
            url = InterfaceDef.CHIHU_SERVER.concat(InterfaceDef.LOGIN);
        }

        Chihu.LoginRequest loginRequest = Chihu.LoginRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();

        RequestBody body = RequestBody.create(JSON, loginRequest.toByteArray());

        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(body);

        String cookie = SharedPreferenceUtil.getInstance().getString(NetworkManager.COOKIE, null);
        if (null != cookie) {
            MLog.i(TAG, cookie);
            builder.addHeader(SET_COOKIE_HEADER, cookie);
        }

        Request request = builder.build();
        return request;

    }

    public static Request getViewCanteensRequest() {
        String url;
        if (CApplication.DEBUG) {
            url = InterfaceDef.CHIHU_DEBUG_SERVER.concat(InterfaceDef.VIEW_CANTEENS);
        } else {
            url = InterfaceDef.CHIHU_SERVER.concat(InterfaceDef.VIEW_CANTEENS);
        }

        Chihu.ViewCanteensRequest request = Chihu.ViewCanteensRequest.newBuilder()
                .build();
        RequestBody body = RequestBody.create(JSON, request.toByteArray());

        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(body);

        String cookie = SharedPreferenceUtil.getInstance().getString(NetworkManager.COOKIE, null);
        if (null != cookie) {
            MLog.i(TAG, cookie);
            builder.addHeader(SET_COOKIE_HEADER, cookie);
        }

        Request httpRequest = builder.build();
        return httpRequest;
    }
}
