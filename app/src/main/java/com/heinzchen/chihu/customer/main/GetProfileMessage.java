package com.heinzchen.chihu.customer.main;

import com.heinzchen.chihu.utils.EventBusMessage;

import protocol.Chihu;

/**
 * Created by chen on 2016/4/8.
 */
public class GetProfileMessage extends EventBusMessage {
    public Chihu.Profile profile;
}
