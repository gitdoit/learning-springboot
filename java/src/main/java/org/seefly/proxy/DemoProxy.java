package org.seefly.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author liujianxin
 * @date 2018-12-06 11:31
 */
public class DemoProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("sfsdfsdf");
        return null;
    }
}
