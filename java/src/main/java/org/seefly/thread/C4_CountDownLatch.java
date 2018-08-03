package org.seefly.thread;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * @author liujianxin
 * @date 2018-07-30 17:15
 * 描述信息：线程倒计时器
 *
 * 如何使线程顺序执行？
 * 可以使用join方法，在各个线程中依次join其他线程，或者在主线程中顺序join各个线程
 * 可以使用ExecutorService创建一个单线程池确保同一时间只有一个线程在执行，线程池的FIFO特性可以确保顺序执行
 * 可以使用共享变量，例如线程1中检查变量为A时开始执行，执行完毕后将变量置为B，线程2中检查变量为B时开始执行....
 * 可以使用Condition，每个线程等待上一个信号量并释放下一个信号量，这个跟上一条思想相同
 *
 * 如何执行这样一个任务，就是指定一个线程的执行条件需要在其他多个线程执行完毕之后执行呢？
 * 这个计数器并不能保证顺序执行，只能保证最后一个执行的顺序
 **/
public class C4_CountDownLatch {

    public static void main(String[] args) throws InterruptedException{
        //计数器倒计时起始值为10，意味着需要倒计时10次
        final CountDownLatch cdl = new CountDownLatch(10);

        ExecutorService es = new ThreadPoolExecutor(10, 10, 500,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), new BasicThreadFactory.Builder().namingPattern("thread-%d").build());
        for (int i = 0; i < 10; i++) {
            es.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "开始准备！");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "准备完毕！");
                //工作执行完毕，退出线程前将计数器减一
                cdl.countDown();
            });
        }

        //主线程在其他线程执行完毕后才能执行
        cdl.await();
        System.out.println("主线程开始工作！");
        es.shutdown();
    }
}
