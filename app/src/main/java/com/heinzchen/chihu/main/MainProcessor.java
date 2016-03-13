package com.heinzchen.chihu.main;

import android.net.ConnectivityManager;

import com.google.protobuf.InvalidProtocolBufferException;
import com.heinzchen.chihu.net.INetCallbackListener;
import com.heinzchen.chihu.net.NetworkManager;
import com.heinzchen.chihu.net.ProtocolManager;
import com.heinzchen.chihu.protocol.Chihu;
import com.heinzchen.chihu.utils.EventBusMessage;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Request;

/**
 * Created by chen on 2016/3/13.
 */
public class MainProcessor implements INetCallbackListener{
    public static final String TAG = MainProcessor.class.getSimpleName();
    private static MainProcessor mInstance;

    private MainProcessor() {

    }

    public static MainProcessor getInstance() {
        if (null == mInstance) {
            synchronized (MainProcessor.class) {
                if (null == mInstance) {
                    mInstance = new MainProcessor();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void onResponse(String pbstr) {
        MLog.i(TAG, "onResponse");
        Chihu.ViewCanteensResponse resp = null;
        try {
            resp = Chihu.ViewCanteensResponse.parseFrom(pbstr.getBytes());
        } catch (InvalidProtocolBufferException e) {
            MLog.e(TAG, e.getMessage());
        }
        if (null == resp) {
            ViewCanteenMessage msg = new ViewCanteenMessage();
            msg.state = EventBusMessage.FAIL;
            msg.msg = "解析错误";
            EventBus.getDefault().post(msg);
            return;
        }

        ViewCanteenMessage msg = new ViewCanteenMessage();
        msg.state = EventBusMessage.SUCCEED;
        msg.canteens = resp.getCanteensList();

        EventBus.getDefault().post(msg);

    }

    public void viewCanteens() {
        Request request = ProtocolManager.getViewCanteensRequest();
        NetworkManager.getInstance().sendRequest(request, this);
    }
}
