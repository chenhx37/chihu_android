package com.heinzchen.chihu.login;

import com.google.protobuf.InvalidProtocolBufferException;
import com.heinzchen.chihu.net.INetCallbackListener;
import com.heinzchen.chihu.net.NetworkManager;
import com.heinzchen.chihu.net.ProtocolManager;
import protocol.Chihu;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Request;

/**
 * Created by chen on 2016/3/12.
 */
public class LoginProcessor implements INetCallbackListener {
    public static final String TAG = LoginProcessor.class.getSimpleName();

    private static LoginProcessor mInstance;

    private LoginProcessor() {
    }

    public static LoginProcessor getInstance() {
        if (null == mInstance) {
            synchronized (LoginProcessor.class) {
                if (null == mInstance) {
                    mInstance = new LoginProcessor();
                }
            }
        }
        return mInstance;
    }

    public void login(String username, String password) {
        Request request = ProtocolManager.getLoginRequest(username, password);
        NetworkManager.getInstance().sendRequest(request, this);

    }

    @Override
    public void onResponse(String pbStr) {
        Chihu.Response response = null;
        try {
            response = Chihu.Response.parseFrom(pbStr.getBytes());
        } catch (InvalidProtocolBufferException e) {
            MLog.e(TAG, e.getMessage());
        }

        //解析错误，登录失败
        if (null == response || !response.hasStatus()) {
            LoginResultMessage msg = new LoginResultMessage();
            msg.result = LoginResultMessage.FAIL;
            msg.msg = "服务器错误";
            EventBus.getDefault().post(msg);//to LoginActivity
            return;
        }


        if (response.getStatus().getNumber() == Chihu.Status.fail_VALUE) {
            //登录失败
            LoginResultMessage msg = new LoginResultMessage();
            msg.result = LoginResultMessage.FAIL;
            if (response.hasMessage()) {
                msg.msg = response.getMessage();
            } else {
                msg.msg = "登录失败";
            }
            EventBus.getDefault().post(msg);//to LoginActivity
        } else if (response.getStatus().getNumber() == Chihu.Status.succeed_VALUE) {
            //登录成功
            LoginResultMessage msg = new LoginResultMessage();
            msg.result = LoginResultMessage.SUCCEED;
            if (response.hasMessage()) {
                msg.msg = response.getMessage();
            } else {
                msg.msg = "登录成功";
            }
            EventBus.getDefault().post(msg);//to LoginActivity
        }
    }

}
