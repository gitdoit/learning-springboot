package org.seefly.thread.lev2;

import org.junit.Test;
import org.seefly.thread.ThreadUtils;

/**
 * 如何主动停止一个正在运行中的线程？
 * 1、通过调用 thread.stop() 来停止，但是由于该方法不安全
 * 已经被标记为废弃。
 * 2、通过设置退出标志位，即在一个loop中循环判断某一变量，外部可以主动改变
 * 该变量致使该线程退出。
 * 3、通过设置中断标志位，即中断线程
 *
 * @author liujianxin
 * @date 2019-07-30 15:07
 */
public class ThreadInterruptDemo {


    /**
     * 中断标志的确是不影响线程的运行，要看线程对于中断标志是如何进行处理的
     * 但是如果处于等待的话，就会抛出异常
     *
     * 忽视中断标志位的线程
     */
    @Test
    public void stopByIgnoreInterrupt(){
        Thread thread = new Thread(() ->{
            while (true){
                System.out.println("i am working!");
            }
        });
        thread.start();
        // 这种方式并不会将线程中断，它只是将中断标志位置为true
        // 至于该线程怎么处理，那就是它的事情了
        thread.interrupt();
        ThreadUtils.sleep(20);
    }

    /**
     * 可以通过自定义的退出标志位来终止线程执行任务
     * 但更优雅的方式是判断原生的中断标志位啊
     * 注意
     * thread.interrupt()的调用会判断当前调用线程是有权限，没有的话会抛出异常
     */
    @Test
    public void testStopInterrupt(){
        Thread thread = new Thread(() ->{
            Thread me = Thread.currentThread();
            // 通过循环判断中断标志位来控制程序的执行
            while (!me.isInterrupted()){
                System.out.println("i am working!");
            }
        });
        thread.start();
        ThreadUtils.sleep(30);
        // 午时已到
        thread.interrupt();
    }

    /**
     * 关于中断标志位的方法有三个，上面已经看到了两个
     * 1、interrupt()
     * 2、IsInterrupted()
     * 这两种方法属于实例的方法，一个相当于set一个相当于get
     * 还有一个是静态方法相当于get+set -> Thread.interrupted()
     * 里面其实是 currentThread().isInterrupted(true); 它这对于当前调用这个方法的线程。
     * 而这个方法的作用就是清除中断标志位，并返回之前的状态
     *
     */
    @Test
    public void testInterrupted(){
        System.out.println("当前线程是否被中断:"+Thread.currentThread().isInterrupted());
        // 将当前线程的中断标志位置为true
        Thread.currentThread().interrupt();
        // 清除中断标志位，并返回之前的中断状态
        System.out.println("当前线程是否被中断:"+Thread.interrupted());
    }

}
