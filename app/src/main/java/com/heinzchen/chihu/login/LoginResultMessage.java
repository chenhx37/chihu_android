package com.heinzchen.chihu.login;

import com.heinzchen.chihu.utils.EventBusMessage;

/**
 * Created by chen on 2016/3/12.
 */
public class LoginResultMessage extends EventBusMessage {
    public int result;
    public int type;
}
