package com.heinzchen.chihu.customer.dishes;

import com.heinzchen.chihu.utils.EventBusMessage;

import protocol.Chihu;

/**
 * Created by chen on 2016/3/19.
 */
public class AddDishMessage extends EventBusMessage {
    public Chihu.Meal meal;
}
