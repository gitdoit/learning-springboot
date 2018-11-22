package org.seefly.juc.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述信息：该类用来演示用lockInterruptibly解除死锁
 * 线程1先获得锁1，然后休眠。这时候线程2获得锁2，休眠
 * 线程1醒了之后去拿锁2，拿不到就挂起了。线程2醒了拿锁1也挂了
 *
 * tryLock()
 * 试图去过去一个锁，若没有拿到则不会进入锁池，直接返回false。
 * 若拿到了则返回true;
 *
 * tryLock(long time,TimeUnit unit)
 * 和上面的方法类似，只是再没有拿到的时候还会等待指定的时间，时间过去则不在等待。
 *
 * lockInterruptibly()
 * 调用这个方法去获取锁时，如果没有拿到锁则跟其他方法一样，进入锁池等待。但是再等待期间
 * 如果当前线程中断位被置为true了，那么该线程则会抛出中断异常从而结束等待。该方法常用来防止死锁
 *
 * @author liujianxin
 * @date 2018-07-30 13:27
 **/
public class ReentrantLockDemo2 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new IntLock(1));
        Thread t2 = new Thread(new IntLock(2));
        //两个线程启动之后则会死锁
        t1.start();
        t2.start();
        Thread.sleep(1000);

        //这里将线程2的中断位置为true，则由于使用了lockInterruptibly方法去获取锁，该线程将会抛出异常并终止
        t2.interrupt();
    }


    private static class IntLock implements Runnable{
        public static ReentrantLock lock1 = new ReentrantLock();
        public static ReentrantLock lock2 = new ReentrantLock();
        int lock;

        public IntLock(int lock){
            this.lock = lock;
        }
        @Override
        public void run() {

            //模拟死锁
            try{
                //若线程1进来了则先获得锁1，休眠后再获得锁2
                if(lock == 1){
                    //获取lock1
                    lock1.lockInterruptibly();
                    try{
                        Thread.sleep(500);
                    }catch (InterruptedException ex){}
                    lock2.lockInterruptibly();
                }else {
                    //若线程2进来了先获得锁2.休眠后在获得锁1
                    lock2.lockInterruptibly();
                    try {
                        Thread.sleep(500);
                    }catch (InterruptedException ex){}
                    lock1.lockInterruptibly();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                //判断当前线程是否持有该锁
                if (lock1.isHeldByCurrentThread()){
                    lock1.unlock();
                }
                if(lock2.isHeldByCurrentThread()){
                    lock2.unlock();
                }
                System.out.println(Thread.currentThread().getId()+":线程退出");
            }

        }
    }
}
