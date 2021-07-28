package org.seefly.aop.basic.proxy.interfaces.impl;

import org.seefly.aop.basic.proxy.interfaces.Call;

/**
 * @author liujianxin
 * @date 2019/8/29 21:24
 */
public class CallImpl implements Call {
    
    @Override
    public void call(String phone) {
        System.out.printf("给%s打电话\r\n", phone);
    }
}
