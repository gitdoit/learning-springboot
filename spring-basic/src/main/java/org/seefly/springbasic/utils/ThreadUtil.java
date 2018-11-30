package org.seefly.springbasic.utils;

import org.apache.commons.lang3.RandomUtils;

import java.util.Random;

/**
 * @author liujianxin
 * @date 2018-11-30 14:55
 */
public class ThreadUtil {
    private ThreadUtil(){}

    public static void RandomSleep(){
        try {
            Thread.sleep(RandomUtils.nextLong(500,2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
