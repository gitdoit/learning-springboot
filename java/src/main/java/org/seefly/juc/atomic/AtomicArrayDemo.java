package org.seefly.juc.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * 数组中元素的原子操作
 * {@link AtomicReferenceArray}
 * {@link AtomicIntegerArray}
 * {@link AtomicLongArray}
 * @author liujianxin
 * @date 2018-11-19 20:13
 */
public class AtomicArrayDemo {

    public static void main(String[] args) throws InterruptedException {
        AtomicIntegerArray air = new AtomicIntegerArray(10);
        int[] arr = new int[10];
        // 并发修改数组中的元素的值，若线程安全，则每个元素都应该是 1000
        for(int i = 0; i < 10; i++){
            new Thread(() ->{
                for(int j = 0 ; j < 1000 ; j++){
                    air.getAndIncrement(j%10);
                    arr[j%10]++;
                }
            }).start();
        }
        Thread.sleep(2000);
        for(int i = 0; i < 10; i++){
            System.out.println("安全："+air.get(i)+"|不安全:"+arr[i]);
        }
    }
}
