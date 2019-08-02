package org.seefly.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程演示AtomicInteger的原子自增特性
 *
 * AtomicInteger 类主要利用 CAS (compare and swap) + volatile 和 native 方法来保证原子操作，从而避免 synchronized 的高开销，执行效率大为提升。
 * CAS的原理是拿期望的值和原本的一个值作比较，如果相同则更新成新的值。
 * UnSafe 类的 objectFieldOffset() 方法是一个本地方法，这个方法是用来拿到“原来的值”的内存地址，返回值是 valueOffset。
 * 另外 value 是一个volatile变量，在内存中可见，因此 JVM 可以保证任何时刻任何线程总能拿到该变量的最新值。
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
