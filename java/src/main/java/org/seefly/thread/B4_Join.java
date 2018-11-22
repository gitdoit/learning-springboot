package org.seefly.thread;

/**
 * 描述信息：本类用来演示join方法
 *  有一时候一个线程的输入或许会依赖另一个或者多个线程的输出，此时需要等待依赖线程执行完毕才能
 *  这个时候可以使用join方法完成这个功能。
 *
 *  join() throws InterruptedException
 *  无参的join方法会使调用它的线程进行无限等待，直到目标线程执行完毕
 *
 *
 *  join(long millis) throws InterruptedException
 *  有参的Join方法指定超时时间，在指定时间内目标线程还未执行完毕，那么将放弃等待继续执行。
 * @author liujianxin
 * @date 2018-07-25 11:04
 **/
public class B4_Join {
    volatile static int  i = 0;
    public static void main(String[] args) throws InterruptedException{
        Thread join = new Thread(() ->{
            for(i = 0; i < 1000000 ; i++){}
        });
        join.start();

        //加入目标线程
        //join.join(2);
        join.join();
        System.out.println(i);
    }
}
