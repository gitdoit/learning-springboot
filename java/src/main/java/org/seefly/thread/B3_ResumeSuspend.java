package org.seefly.thread;

/**
 * 描述信息：该类用来演示两个已经被废弃的方法为什么会被废弃 = =
 * @author liujianxin
 * @date 2018-07-25 09:55
 **/
public class B3_ResumeSuspend {
    private static Object block = new Object();


    public static void main(String[] args) throws InterruptedException{
        Runnable runnable = () ->{
            System.out.println("正在执行线程："+Thread.currentThread().getName());
            Thread.currentThread().suspend();
        };

        Thread t1= new Thread(runnable,"t1");
        Thread t2= new Thread(runnable,"t2");

        // 开启线程1，执行时会被挂起
        t1.start();
        //主线程休眠，防止线程2先启动
        Thread.sleep(100);
        //启动线程2
        t2.start();
        //释放线程1
        t1.resume();
        //这一步会有问题，当执行到这一步的时候可能线程2还没有启动。那么提前
        //使用resume使线程继续执行会不在对应的时机，那么线程2将被被永远挂起
        //更坑的是该线程的状态是Runnable
        t2.resume();
        t1.join();
        t2.join();
    }
}
