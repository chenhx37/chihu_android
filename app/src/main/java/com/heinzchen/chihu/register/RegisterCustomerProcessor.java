package com.heinzchen.chihu.register;

import com.google.protobuf.InvalidProtocolBufferException;
import com.heinzchen.chihu.net.INetCallbackListener;
import com.heinzchen.chihu.net.NetworkManager;
import com.heinzchen.chihu.net.ProtocolManager;
import protocol.Chihu;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Request;

/**
 * Created by chen on 2016/3/10.
 */
public class RegisterCustomerProcessor implements INetCallbackListener {
    public static final String TAG = RegisterCustomerProcessor.class.getSimpleName();
    private RegisterCustomerProcessor() {
    }

    private static RegisterCustomerProcessor mInstance;

    public static RegisterCustomerProcessor getInstance() {
        if (null == mInstance) {
            synchronized (RegisterCustomerProcessor.class) {
                if (null == mInstance) {
                    mInstance = new RegisterCustomerProcessor();
                }
            }
        }
        return mInstance;
    }

    public void registerUser(String username, String password, String email, String school, String type) {
        Request request = ProtocolManager.getRegisterUserRequest(username, password, email, school, type);
        NetworkManager.getInstance().sendRequest(request, this);
    }

    @Override
    public void onResponse(String pbString) {
        Chihu.Response response1 = null;
        try {
            response1 = Chihu.Response.parseFrom(pbString.getBytes());
            MLog.i(TAG, response1.getStatus().name());
            MLog.i(TAG, response1.getMessage());
        } catch (InvalidProtocolBufferException e) {
            MLog.e(TAG, e.getMessage());
        }

        if (null == response1) {
            MLog.e(TAG, "null==response1");
            RegisterCustomerResponseMessage msg = new RegisterCustomerResponseMessage();
            msg.result = RegisterCustomerResponseMessage.FAIL;
            msg.msg = "服务器错误";
            EventBus.getDefault().post(msg);//to RegisterCustomerActivity
            return;
        }

        if (!response1.hasStatus()) {
            MLog.e(TAG, "!response1.hasStatus()");
            RegisterCustomerResponseMessage msg = new RegisterCustomerResponseMessage();
            msg.result = RegisterCustomerResponseMessage.FAIL;
            msg.msg = "服务器错误";
            EventBus.getDefault().post(msg);//to RegisterCustomerActivity
            return;
        }

//        String status = response1.getStatus().name();
        int status = response1.getStatus().getNumber();
        if (status == Chihu.Status.fail_VALUE) {
            MLog.e(TAG, "status = fail");
            RegisterCustomerResponseMessage msg = new RegisterCustomerResponseMessage();
            msg.result = RegisterCustomerResponseMessage.FAIL;
            msg.msg = response1.getMessage();
            EventBus.getDefault().post(msg);//to RegisterCustomerActivity
        } else if (status == Chihu.Status.succeed_VALUE) {
            MLog.i(TAG, "status = succeed");
            RegisterCustomerResponseMessage msg = new RegisterCustomerResponseMessage();
            msg.result = RegisterCustomerResponseMessage.SUCCEED;
            msg.msg = response1.getMessage();
            EventBus.getDefault().post(msg);//to RegisterCustomerActivity
        }


    }
}
