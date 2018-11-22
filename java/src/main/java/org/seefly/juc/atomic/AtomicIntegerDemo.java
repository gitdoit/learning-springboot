package org.seefly.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程演示AtomicInteger的原子自增特性
 *
 * @author liujianxin
 * @date 2018-11-19 14:22
 */
public class AtomicIntegerDemo {
    private static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        // 临界区资源
        AtomicInteger ai = new AtomicInteger(0);
        // 这个方法并不是原子的
        //ai.set();
        // 开十个线程，演示并发修改临界区资源
        for(int i = 0 ; i< 10 ; i++){
            new Thread(() ->{
                for(int j = 0 ; j < 1000; j++){
                    // 底层使用sun.misc.Unsafe.getAndAddInt
                    ai.incrementAndGet();
                    count++;
                }
            }).start();
        }
        Thread.sleep(5000);
        System.out.println("普通："+count);
        System.out.println("原子自增："+ai.get());
    }

}
