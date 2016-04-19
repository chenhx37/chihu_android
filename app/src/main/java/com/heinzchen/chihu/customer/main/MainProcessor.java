package com.heinzchen.chihu.customer.main;

import com.google.protobuf.InvalidProtocolBufferException;
import com.heinzchen.chihu.customer.dishes.ViewMealsMessage;
import com.heinzchen.chihu.net.INetCallbackListener;
import com.heinzchen.chihu.net.NetworkManager;
import com.heinzchen.chihu.net.ProtocolManager;
import protocol.Chihu;
import com.heinzchen.chihu.utils.EventBusMessage;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Request;

/**
 * Created by chen on 2016/3/13.
 */
public class MainProcessor {

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


    public void viewCanteens() {
        Request request = ProtocolManager.getViewCanteensRequest();
        NetworkManager.getInstance().sendRequest(request, new INetCallbackListener() {
            @Override
            public void onResponse(String pbString) {
                MLog.i(TAG, "onResponse");
                Chihu.ViewCanteensResponse resp = null;
                MLog.e(TAG, (new Integer(pbString.getBytes().length)).toString());

                try {
                    resp = Chihu.ViewCanteensResponse.parseFrom(pbString.getBytes());
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
        });
    }

    public void viewMeals(int canteenId) {
        MLog.i(TAG, "viewMeals");
        Request request = ProtocolManager.getViewMealsRequest(canteenId);
        NetworkManager.getInstance().sendRequest(request, new INetCallbackListener() {
            @Override
            public void onResponse(String pbString) {
                MLog.i(TAG, "onResponse,viewMeals");
                Chihu.ViewMealsResponse resp = null;
                MLog.e(TAG, (new Integer(pbString.getBytes().length)).toString());
                try {
                    resp = Chihu.ViewMealsResponse.parseFrom(pbString.getBytes());
                } catch (InvalidProtocolBufferException e) {
                    MLog.e(TAG, e.getMessage());
                }

                if (null == resp) {
                    ViewMealsMessage msg = new ViewMealsMessage();
                    msg.state = EventBusMessage.FAIL;
                    msg.msg = "解析错误";
                    EventBus.getDefault().post(msg);//to DishesActivity
                    return;
                }
                ViewMealsMessage msg = new ViewMealsMessage();
                msg.state = EventBusMessage.SUCCEED;
                msg.meals = resp.getMealsList();
                EventBus.getDefault().post(msg);//to DishesActivity

            }
        });

    }
}
