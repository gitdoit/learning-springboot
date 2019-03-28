package org.seefly.guava;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author liujianxin
 * @date 2019-03-05 19:22
 */
public class RateLimiterDemo {

    @Test
    public void testLimiter() throws InterruptedException {
        //每秒最多允许93次请求
        RateLimiter rateLimiter = RateLimiter.create(9);


        //构建100个调用，同时发出
        for (int i = 0; i < 105; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                        //检查频率
                        //Preconditions.checkState(rateLimiter.acquire(), "令牌不足则等待");
                        //Preconditions.checkState(rateLimiter.tryAcquire(), "令牌不足则立即返回");
                        rateLimiter.acquire();
                        //频率检查通过，执行业务代码
                        System.out.println("业务执行中"+System.currentTimeMillis() / 1000);

                }
            }).start();
        }
        System.out.println("wait...");
        Thread.sleep(5000);


    }
}
