package org.seefly.springaop.proxy.staticc;

import org.seefly.springaop.proxy.Call;
import org.seefly.springaop.proxy.CallImpl;
import org.seefly.springaop.proxy.Sms;

/**
 *
 * 代理模式
 * 目标对象提供一个代理对象，通过访问代理对象来控制对目标对象的访问。
 * 通过这种方式可以方便的实现原有业务的扩展，以及避免了直接访问原有对象的复杂性。
 * 代理模式需要代理对象和目标对象实现同样的接口。
 *
 * 当前使用的代理模式是静态代理模式
 * 通过硬编码的形式使代理对象和目标对象实现同一个接口，这样的做法
 * 违反了开闭原则(对扩展开放，对修改关闭)，如果有新的业务功能需要增加，例如发邮件
 * 那么只能再继续实现发邮件接口。
 *
 *
 *
 *
 *
 * @author liujianxin
 * @date 2019/8/29 21:11
 */
public class ServiceCenter implements Call , Sms {
    private Call callProxy;


    public static void main(String[] args) {
        ServiceCenter serviceCenter = new ServiceCenter(new CallImpl());
        serviceCenter.call("110");
    }



    public ServiceCenter(Call callProxy) {
        this.callProxy = callProxy;
    }

    @Override
    public void call(String phone) {
        before();
        callProxy.call(phone);
        after();
    }

    @Override
    public void sendSms(String phone) {
        // 同上
    }

    private void before(){
        System.out.println("执行前！");
    }

    private void after(){
        System.out.println("执行后！");
    }


}
