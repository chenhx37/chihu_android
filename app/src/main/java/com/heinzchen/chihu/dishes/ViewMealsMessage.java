package com.heinzchen.chihu.dishes;

import com.heinzchen.chihu.utils.EventBusMessage;

import java.util.List;

import protocol.Chihu;

/**
 * Created by chen on 2016/3/15.
 */
public class ViewMealsMessage extends EventBusMessage {
    public int state;
    public List<Chihu.Meal> meals;

}
