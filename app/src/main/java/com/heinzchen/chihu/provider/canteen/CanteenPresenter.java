package com.heinzchen.chihu.provider.canteen;

import com.google.protobuf.InvalidProtocolBufferException;
import com.heinzchen.chihu.net.INetCallbackListener;
import com.heinzchen.chihu.net.NetworkManager;
import com.heinzchen.chihu.net.ProtocolManager;
import com.heinzchen.chihu.utils.EventBusMessage;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Request;
import protocol.Chihu;

/**
 * Created by chen on 2016/4/16.
 */
public class CanteenPresenter {
    public static final String TAG = CanteenPresenter.class.getSimpleName();
    private static CanteenPresenter mInstance;

    private CanteenPresenter() {

    }
    public static CanteenPresenter getInstance() {
        if (null == mInstance) {
            synchronized (CanteenPresenter.class) {
                if (null == mInstance) {
                    mInstance = new CanteenPresenter();
                }
            }
        }
        return mInstance;
    }

    public void getCanteens() {
        Request request = ProtocolManager.getCanteens();
        NetworkManager.getInstance().sendRequest(request, new INetCallbackListener() {
            @Override
            public void onResponse(String pbString) {
                Chihu.ViewCanteensResponse response = null;
                try {
                    response = Chihu.ViewCanteensResponse.parseFrom(pbString.getBytes());
                } catch (InvalidProtocolBufferException e) {
                    MLog.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
                if (null != response) {
                    GetCanteenResult result = new GetCanteenResult();
                    result.canteens = response.getCanteensList();
                    EventBus.getDefault().post(result);//to CanteenFragment
                }
            }
        });
    }

    public void getDishes(int canteenId) {
        Request request = ProtocolManager.getDishes(canteenId);
        NetworkManager.getInstance().sendRequest(request, new INetCallbackListener() {
            @Override
            public void onResponse(String pbString) {
                Chihu.ViewMealsResponse response = null;
                try {
                    response = Chihu.ViewMealsResponse.parseFrom(pbString.getBytes());
                } catch (InvalidProtocolBufferException e) {
                    MLog.e(TAG, e.getMessage());
                }
                if (null == response) {
                    return;
                }

                GetDishesResult result = new GetDishesResult();
                result.dishes = response.getMealsList();
                EventBus.getDefault().post(result);
            }
        });
    }

    public void addDish(String name, String price,int canteenId) {
        Request request = ProtocolManager.addDish(name, price, canteenId);

        NetworkManager.getInstance().sendRequest(request, new INetCallbackListener() {
            @Override
            public void onResponse(String pbString) {
                Chihu.Response response = null;
                try {
                    response = Chihu.Response.parseFrom(pbString.getBytes());
                } catch (InvalidProtocolBufferException e) {
                    MLog.e(TAG, e.getMessage());
                }
                int res;
                if (null == response) {
                    res = EventBusMessage.FAIL;
                }else {
                    res = EventBusMessage.SUCCEED;
                }

                MLog.i(TAG, String.valueOf(res));
                AddDishResult result = new AddDishResult();
                result.result = res;
                EventBus.getDefault().post(result);// to AddDishActivity
            }
        });
    }

    public void addCanteen(String name) {
        Request request = ProtocolManager.addCanteen(name);

        NetworkManager.getInstance().sendRequest(request, new INetCallbackListener() {
            @Override
            public void onResponse(String pbString) {
                Chihu.Response response = null;
                try {
                    response = Chihu.Response.parseFrom(pbString.getBytes());
                } catch (InvalidProtocolBufferException e) {
                    MLog.e(TAG, e.getMessage());
                }
                int res;
                if (null == response) {
                    res = EventBusMessage.FAIL;
                } else {
                    res = EventBusMessage.SUCCEED;
                }

                MLog.i(TAG, String.valueOf(res));
                AddCanteenResult result = new AddCanteenResult();
                result.result = res;
                EventBus.getDefault().post(result);//to AddCanteenActivity

            }
        });
    }


}
