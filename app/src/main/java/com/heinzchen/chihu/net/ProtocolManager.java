package com.heinzchen.chihu.net;

import com.heinzchen.chihu.CApplication;
import protocol.Chihu;

import com.heinzchen.chihu.account.ProfileManager;
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

    public static Request getRegisterUserRequest(String username, String password, String email, String school, String type) {

        String url;
        if (CApplication.DEBUG) {
            url = InterfaceDef.CHIHU_DEBUG_SERVER.concat(InterfaceDef.REGISTER_USER);
        } else {
            url = InterfaceDef.CHIHU_SERVER.concat(InterfaceDef.REGISTER_USER);
        }

        Chihu.UserType userType;
        MLog.i(TAG, type);
        if (type.equals("顾客用户")) {
            userType = Chihu.UserType.CUSTOMER;
        } else {
            userType = Chihu.UserType.PROVIDER;
        }

        Chihu.UserAccount.Builder builder = Chihu.UserAccount.newBuilder();
        builder.setUsername(username);
        builder.setPassword(password);
        builder.setEmail(email);
        builder.setSchool(Chihu.School.valueOf(Chihu.School.Sun_Yat_San_University_VALUE));
        builder.setType(userType);

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

    public static Request getViewMealsRequest(int canteenId) {
        String url;
        if (CApplication.DEBUG) {
            url = InterfaceDef.CHIHU_DEBUG_SERVER.concat(InterfaceDef.VIEW_MEALS);
        } else {
            url = InterfaceDef.CHIHU_SERVER.concat(InterfaceDef.VIEW_MEALS);
        }
        Chihu.ViewMealsRequest request_pb = Chihu.ViewMealsRequest.newBuilder()
                .setCanteenId(canteenId)
                .build();

        RequestBody body = RequestBody.create(JSON, request_pb.toByteArray());
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

    public static Request updateProfile(String username, String email, String netId, String phone, String receiver, String address) {
        String url;
        if (CApplication.DEBUG) {
            url = InterfaceDef.CHIHU_DEBUG_SERVER.concat(InterfaceDef.UPDATE_PROFILE);
        } else {
            url = InterfaceDef.CHIHU_SERVER.concat(InterfaceDef.UPDATE_PROFILE);
        }

        Chihu.Profile preProfile = ProfileManager.getInstance().getCachedProfile();
        Chihu.UserType type = Chihu.UserType.CUSTOMER;
        if (null != preProfile) {
            type = preProfile.getType();
        }

        Chihu.Profile profile = Chihu.Profile.newBuilder()
                .setUsername(username)
                .setEmail(email)
                .setNetid(netId)
                .setPhone(phone)
                .setReceiver(receiver)
                .setAddress(address)
                .setType(type)
                .build();

        RequestBody body = RequestBody.create(JSON, profile.toByteArray());
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(body);
        String cookie = SharedPreferenceUtil.getInstance().getString(NetworkManager.COOKIE, null);
        if (null != cookie) {
            builder.addHeader(SET_COOKIE_HEADER, cookie);
        }
        Request httpRequest = builder.build();
        return httpRequest;
    }

    public static Request getProfile() {
        String url;
        if (CApplication.DEBUG) {
            url = InterfaceDef.CHIHU_DEBUG_SERVER.concat(InterfaceDef.GET_PROFILE);
        } else {
            url = InterfaceDef.CHIHU_SERVER.concat(InterfaceDef.GET_PROFILE);
        }
        Request.Builder builder = new Request.Builder();

        RequestBody body = RequestBody.create(JSON, "");
        builder.url(url)
                .post(body);

        String cookie = SharedPreferenceUtil.getInstance().getString(NetworkManager.COOKIE, null);
        if (null != cookie) {
            builder.addHeader(SET_COOKIE_HEADER, cookie);
        }
        Request httpRequest = builder.build();
        return httpRequest;

    }

    public static Request getCanteens() {
        String url;
        if (CApplication.DEBUG) {
            url = InterfaceDef.CHIHU_DEBUG_SERVER.concat(InterfaceDef.GET_CANTEENS);
        } else {
            url = InterfaceDef.CHIHU_SERVER.concat(InterfaceDef.GET_CANTEENS);
        }

        Request.Builder builder = new Request.Builder();

        RequestBody body = RequestBody.create(JSON, "");
        builder.url(url)
                .post(body);
        String cookie = SharedPreferenceUtil.getInstance().getString(NetworkManager.COOKIE, null);
        if (null != cookie) {
            builder.addHeader(SET_COOKIE_HEADER, cookie);
        }
        return builder.build();
    }

    public static Request getDishes(int canteenId) {
        String url;
        if (CApplication.DEBUG) {
            url = InterfaceDef.CHIHU_DEBUG_SERVER.concat(InterfaceDef.VIEW_MEALS);
        } else {
            url = InterfaceDef.CHIHU_SERVER.concat(InterfaceDef.VIEW_MEALS);
        }
        Chihu.ViewMealsRequest request_pb = Chihu.ViewMealsRequest.newBuilder()
                .setCanteenId(canteenId)
                .build();

        RequestBody body = RequestBody.create(JSON, request_pb.toByteArray());
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

    public static Request addDish(String name, String price, int canteenId) {
        String url;
        if (CApplication.DEBUG) {
            url = InterfaceDef.CHIHU_DEBUG_SERVER.concat(InterfaceDef.ADD_DISH);
        } else {
            url = InterfaceDef.CHIHU_SERVER.concat(InterfaceDef.ADD_DISH);
        }

        Chihu.AddDishRequest req = Chihu.AddDishRequest.newBuilder()
                .setName(name)
                .setPrice(price)
                .setCanteenId(String.valueOf(canteenId))
                .build();

        RequestBody body = RequestBody.create(JSON, req.toByteArray());
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(body);

        String cookie = SharedPreferenceUtil.getInstance().getString(NetworkManager.COOKIE, null);
        if (null != cookie) {
            MLog.i(TAG, cookie);
            builder.addHeader(SET_COOKIE_HEADER, cookie);
        }

        return builder.build();
    }

    public static Request addCanteen(String name) {
        String url;
        if (CApplication.DEBUG) {
            url = InterfaceDef.CHIHU_DEBUG_SERVER.concat(InterfaceDef.ADD_CANTEEN);
        } else {
            url = InterfaceDef.CHIHU_SERVER.concat(InterfaceDef.ADD_CANTEEN);
        }

        Chihu.AddCanteenRequest req = Chihu.AddCanteenRequest.newBuilder()
                .setName(name)
                .build();

        RequestBody body = RequestBody.create(JSON, req.toByteArray());
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(body);

        String cookie = SharedPreferenceUtil.getInstance().getString(NetworkManager.COOKIE, null);
        if (null != cookie) {
            MLog.i(TAG, cookie);
            builder.addHeader(SET_COOKIE_HEADER, cookie);
        }

        return builder.build();
    }

    public static Request getProviderOrders() {
        String url;
        if (CApplication.DEBUG) {
            url = InterfaceDef.CHIHU_DEBUG_SERVER.concat(InterfaceDef.GET_PROVIDER_ORDER);
        } else {
            url = InterfaceDef.CHIHU_SERVER.concat(InterfaceDef.GET_PROVIDER_ORDER);
        }
        Request.Builder builder = new Request.Builder();

        RequestBody body = RequestBody.create(JSON, "");
        builder.url(url)
                .post(body);

        String cookie = SharedPreferenceUtil.getInstance().getString(NetworkManager.COOKIE, null);
        if (null != cookie) {
            builder.addHeader(SET_COOKIE_HEADER, cookie);
        }
        Request httpRequest = builder.build();
        return httpRequest;
    }
}
