package org.seefly.juc;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * @author liujianxin
 * @date 2018-07-30 15:48
 * 描述信息：该类用来演示信号量
 * 在创建信号量时必须指定可以有多少线程并发申请信号量
 * public Semaphore(int permits)
 * public Semaphore(int permits,boolean fair)
 * <p>
 * voi acquire()
 * 申请许可,若没有，则一直等待，直到获得许可或者被中断
 * <p>
 * void acquireUniterruptibly()
 * 和上面的方法类似，但是这个不响应中断
 * <p>
 * boolean tryAcquire()
 * 尝试获得许可，成功返回true,失败返回false
 * <p>
 * boolean tryAcquire(long timeout,TimeUnit unit)
 * <p>
 * void release()
 * 释放资源，对于获得许可的线程，在执行完毕之后一定要释放。否则信号量会越来越少
 **/
public class SemaphoreDemo {

    public static void main(String[] args) {
        final Semaphore semp = new Semaphore(5);
        ExecutorService exc = new ThreadPoolExecutor(20, 20, 500,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), new BasicThreadFactory.Builder().namingPattern("thread-%d").build());

        //由于信号量设置为5，也就是同时可以有5个线程进入临界资源区域。现象就是以5个每组的打印信息
        for (int i = 0; i < 20; i++) {
            exc.submit(() -> {
                try {
                    semp.acquire();
                    Thread.sleep(2000);
                    System.out.println(Thread.currentThread().getName() + "工作完毕！");
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    semp.release();
                }
            });
        }

    }

}
