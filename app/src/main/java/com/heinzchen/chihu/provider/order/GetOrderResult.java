package com.heinzchen.chihu.provider.order;

import com.heinzchen.chihu.utils.EventBusMessage;

import java.util.List;

import protocol.Chihu;

/**
 * Created by chen on 2016/4/18.
 */
public class GetOrderResult extends EventBusMessage {
    public List<Chihu.Order> orders;
}
