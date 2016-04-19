package com.heinzchen.chihu.provider.order;

import com.google.protobuf.InvalidProtocolBufferException;
import com.heinzchen.chihu.net.INetCallbackListener;
import com.heinzchen.chihu.net.NetworkManager;
import com.heinzchen.chihu.net.ProtocolManager;
import com.heinzchen.chihu.provider.canteen.AddCanteenResult;
import com.heinzchen.chihu.provider.canteen.AddDishResult;
import com.heinzchen.chihu.provider.canteen.GetCanteenResult;
import com.heinzchen.chihu.provider.canteen.GetDishesResult;
import com.heinzchen.chihu.utils.EventBusMessage;
import com.heinzchen.chihu.utils.MLog;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Request;
import protocol.Chihu;

/**
 * Created by chen on 2016/4/18.
 */
public class OrderPresenter {

    public static final String TAG = OrderPresenter.class.getSimpleName();
    private static OrderPresenter mInstance;

    private OrderPresenter() {

    }
    public static OrderPresenter getInstance() {
        if (null == mInstance) {
            synchronized (OrderPresenter.class) {
                if (null == mInstance) {
                    mInstance = new OrderPresenter();
                }
            }
        }
        return mInstance;
    }

    public void getProviderOrders() {
        Request request = ProtocolManager.getProviderOrders();
        NetworkManager.getInstance().sendRequest(request, new INetCallbackListener() {
            @Override
            public void onResponse(String pbString) {
                Chihu.ProviderGetOrderResponse response = null;
                try {
                    response = Chihu.ProviderGetOrderResponse.parseFrom(pbString.getBytes());
                } catch (InvalidProtocolBufferException e) {
                    MLog.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
                if (null != response) {
                    GetOrderResult result = new GetOrderResult();
                    result.orders = response.getOrdersList();
                    EventBus.getDefault().post(result);//to OrderFragment
                }
            }
        });
    }

}
