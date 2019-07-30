package org.seefly.thread.lev2;

import org.junit.Test;
import org.seefly.thread.ThreadUtils;

/**
 * 线程组主要维度
 * 1、名称
 * 2、优先级
 *      关于优先级，线程组内的所有线程其优先级不能高于所属线程组的优先级
 *  并且当前线程组的优先级不能高于父线程组的优先级。
 * 3、是否守护
 * 4、父线程组
 *      关于父线程组，如果不指定则父线程组为当前线程所属的线程组
 * 5、子线程组
 *
 * 关于线程的异常处理
 * 由于多线程中发生异常无法通过普通的try-catch进行捕获，所以需要通过设置
 * 线程未捕获异常处理器来处理这些未处理异常。
 * 首先Thread类中有两种异常处理方式
 * 1、每个线程专属的未捕获线程处理器
 * 2、全局的默认未捕获异常处理器
 *
 * OK，那么多线程发生未捕获异常处理时的步骤
 * 1、由jvm调用当前线程的未捕获异常处理器 thread.getUncaughtExceptionHandler()
 * 2、如果当前线程没有设置未捕获异常处理器，那么就返回当前线程数所属线程组(线程组实现了未捕获异常处理接口)
 * 3、默认情况下线程组内的未捕获异常处理逻辑为
 *      1、如果有父线程组，则调用父线程组的uncaughtException(t, e)
 *      2、否则判断是否有全局的默认未捕获异常处理器，有则调用，没有则打印异常信息
 * 所以整个未捕获异常处理流程为 当前线程未捕获异常处理 -> 线程组未捕获异常处理 -> 默认全局未捕获异常处理 -> 打印错误流
 *
 * 注意：
 * 1
 *
 * @author liujianxin
 * @date 2019-07-30 13:26
 */
public class ThreadExceptionDemo {


    /**
     * 当前线程自定义的未捕获异常处理器
     */
    @Test
    public void testException(){
        Thread thread = new Thread(()->{
            int a = 1 / 0;
        });
        // 给当前线程设置未捕获异常处理
        thread.setUncaughtExceptionHandler(((t, e) -> {
            // 当前发生未捕获异常的线程还处于RUNNABLE状态
            System.out.println(Thread.currentThread().getState());
            // 注意，这个未捕获异常的处理还是在当前线程中
            System.out.println(Thread.currentThread().getName());
            System.out.println(t.getName()+":"+e.getMessage());


            // 疑问？那我这里能不能继续调用它的run方法？ 这样就死循环了把
            // 不，这里调用run方法还是抛异常啊，然后就挂了，没有地方可以捕获
            t.run();
        }));
        thread.start();
        ThreadUtils.sleep(20);
        System.out.println(thread.getState());
    }


    /**
     * 线程组的未捕获异常处理器
     */
    @Test
    public void testExceptionGroup(){
        ThreadGroup threadGroup = new ThreadGroup("ex"){
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(Thread.currentThread().getName());
                System.out.println(t.getName()+":"+e.getMessage());
            }
        };
        Thread thread = new Thread(threadGroup,() ->{
            int a = 1 / 0;
        });
        thread.start();
        ThreadUtils.sleep(200);
    }

    @Test
    public void testDefaultExceptionHandle(){
        // 这样跑直接就结束了，我猜是运行这个任务的线程和主线程不在一个线程组内
        testRun();
    }

    public static void main(String[] args) {
        // 这样就行
        testRun();
    }

     private static void testRun(){
        Thread.setDefaultUncaughtExceptionHandler((t,e) ->{
            System.out.println(Thread.currentThread().getName());
            System.out.println(t.getName()+":"+e.getMessage());
        });
        Thread thread = new Thread(() ->{
            ThreadUtils.sleep(5000);
            System.out.println(Thread.currentThread().getThreadGroup().getName());
            int a = 1 / 0;
        });
        thread.start();
    }



}
