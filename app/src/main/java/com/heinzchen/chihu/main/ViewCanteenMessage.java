package com.heinzchen.chihu.main;

import com.heinzchen.chihu.protocol.Chihu;
import com.heinzchen.chihu.utils.EventBusMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2016/3/13.
 */
public class ViewCanteenMessage extends EventBusMessage {
    public int state;
    public List<Chihu.Canteen> canteens;
}
