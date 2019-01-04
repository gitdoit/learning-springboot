package org.seefly.springnetwork;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author liujianxin
 * @date 2019-01-04 13:59
 */
public class WithoutSpringTest {
    @Test
    public void testInetAddress() throws UnknownHostException {
        InetAddress[] allByName = InetAddress.getAllByName("baidu.com");
        System.out.println(allByName.length);
        for(InetAddress inetAddress : allByName){
            //123.125.115.110 220.181.57.216
            System.out.println(inetAddress.getHostAddress());
            //baidu.com
            System.out.println(inetAddress.getHostName());
        }
    }

    @Test
    public void testStaticGetLocalHost() throws UnknownHostException {
        //DESKTOP-P875S4B/192.168.0.170
        System.out.println(InetAddress.getLocalHost());
    }
}
