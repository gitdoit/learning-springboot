package org.seefly.proxy;

import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * @author liujianxin
 * @date 2018-12-06 11:34
 */
public class ProxyTest {
    @Test
    public void testProxy(){
        DemoInterface o = (DemoInterface)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{DemoInterface.class}, new DemoProxy());
        o.sayHello();
    }
}
