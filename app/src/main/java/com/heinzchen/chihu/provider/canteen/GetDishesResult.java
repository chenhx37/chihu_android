package com.heinzchen.chihu.provider.canteen;

import com.heinzchen.chihu.utils.EventBusMessage;

import java.util.List;

import protocol.Chihu;

/**
 * Created by chen on 2016/4/17.
 */
public class GetDishesResult extends EventBusMessage {
    List<Chihu.Meal> dishes;
}
