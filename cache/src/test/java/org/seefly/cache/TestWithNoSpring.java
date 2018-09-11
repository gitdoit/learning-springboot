package org.seefly.cache;

import org.junit.Test;

/**
 * @author liujianxin
 * @date 2018-09-04 15:28
 */
public class TestWithNoSpring {

    @Test
    public void test1(){
        System.out.println(Math.pow(10.0, (27.55 - (20 * Math.log10(2412)) + Math.abs(-56)) / 20.0));
        int rssi = 65;
        //double distance = Math.pow(10.0, (27.55 - (20 * Math.log10(2412)) + rssi) / 20.0) ;
        double distance = Math.pow(10.0, (rssi - 20 * Math.log10(2412) - 32.44) / 20) * 1000;
        System.out.println(distance);
    }
}
