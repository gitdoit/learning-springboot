package org.seefly.aop.basic.proxy.interfaces.impl;

import org.seefly.aop.basic.proxy.interfaces.Sms;

/**
 * @author liujianxin
 * @date 2019/8/29 21:24
 */
public class SmsImpl implements Sms {
    
    @Override
    public void sendSms(String phone) {
        System.out.printf("给%s发送短信\r\n", phone);
    }
}
