package org.seefly.aop.basic.proxy.statics;

import lombok.extern.slf4j.Slf4j;
import org.seefly.aop.basic.proxy.interfaces.Call;
import org.seefly.aop.basic.proxy.interfaces.impl.CallImpl;
import org.seefly.aop.basic.proxy.interfaces.Sms;
import org.seefly.aop.basic.proxy.interfaces.impl.SmsImpl;

/**
 * 硬编码的委托代理模式
 *
 * 代理模式
 *      目标对象提供一个代理对象，通过访问代理对象来控制对目标对象的访问。
 *      通过这种方式可以方便的实现原有业务的扩展，以及避免了直接访问原有对象的复杂性。
 *
 * 代理模式需要代理对象和目标对象实现同样的接口。
 * 当前使用的代理模式是静态代理模式 通过硬编码的形式使代理对象和目标对象实现同一个接口，
 * 这样的做法 违反了开闭原则(对扩展开放，对修改关闭)，如果有新的业务功能需要增加，例如发邮件 那么只能再继续实现发邮件接口。
 *
 * 扩展-代理模式和委托模式的区别
 *    They are quite similar conceptually but there are differences.
 *    In proxy pattern we use inheritance to provide a proxy object that appears in place of the original object.
 *    The delegation pattern is all about composition - the work is delegated to the composed object.
 *
 * @author liujianxin
 * @date 2019/8/29 21:11
 */
@Slf4j
public class StaticProxyDemo implements Call, Sms {
    
    private final Call realCall;
    private final Sms realSms;
    
    
    public static void main(String[] args) {
        StaticProxyDemo staticProxyDemo = new StaticProxyDemo(new CallImpl(),new SmsImpl());
        staticProxyDemo.call("110");
        staticProxyDemo.sendSms("112");
    }
    
    
    public StaticProxyDemo(Call realCall,Sms realSms) {
        this.realCall = realCall;
        this.realSms = realSms;
    }
    
    @Override
    public void call(String phone) {
        before();
        realCall.call(phone);
        after();
    }
    
    @Override
    public void sendSms(String phone) {
        before();
        realSms.sendSms(phone);
        after();
    }
    
    
    
    private void before() {
        log.info("[StaticProxyDemo] 执行前");
    }
    
    private void after() {
        log.info("[StaticProxyDemo] 执行后");
    }
    
    
}
