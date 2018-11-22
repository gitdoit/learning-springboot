package org.seefly.juc.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liujianxin
 * @date 2018-07-30 14:31
 * 描述信息：演示如何使用ReentrantLock的Condition
 *  ReentrantLock的newCondition()方法，可以创建信号量
 *
 *  Condition对象相较于之前的Object中的一些同步控制方法比较
 *  Condition                               Object
 *  await:等待指定条件的唤醒                  wait：等待对于该锁的唤醒
 *  signal：唤醒等待该条件的对象              notify:唤醒等待该锁的线程
 *
 *  可以看到，Condition是对wait以及Notify的拓展，它以更加细粒度的方式来控制锁的获取与释放
 *  例如某线程只等待某一个条件满足之后才被唤醒。而wait方法却无法做到，它以简单粗暴的方式唤醒线程，线程被唤醒之后检查条件
 *  发现不满足运行要求，从而继续等待。
 *
 *  注意：和wait,notify方法一样，在使用前一定要先获取锁。
 *
 **/
public class LockByConditionDemo {

    public static void main(String[] args) throws InterruptedException{
        final SysQueue sq = new SysQueue(50);

        new Thread(() ->{
            int i = 0;
            while (i++ < 1000){
                try {
                    sq.add(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"add").start();

        new Thread(() ->{
            int i = 0;
            while (i++ < 1000){
                try {
                    sq.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"get").start();
        Thread.sleep(4000);
        for(Integer i : sq.list){
            System.out.println(i);
        }
    }


    /**
     * 自定义一个同步线程容器
     */
    private static class SysQueue {

        private final List<Integer> list;
        private final int capacity;
        private int index = 0;

        private final ReentrantLock lock = new ReentrantLock();
        /**
         * 非满条件
         */
        private final Condition notFull = lock.newCondition();
        /**
         * 非空条件
         */
        private final Condition notEmpty = lock.newCondition();

        public SysQueue(int capacity) {
            assert capacity > 0 && capacity < Integer.MAX_VALUE;
            this.capacity = capacity;
            this.list = new ArrayList<>(capacity);
        }

        /**
         * 添加操作，判断容器是否已满。
         * 添加后唤醒非空信号量
         * @param e
         * @throws InterruptedException
         */
        public void add(int e) throws InterruptedException {
            //上锁
            this.lock.lockInterruptibly();
            try {
                //若容器已满,则释放锁等待非满信号
                while (this.capacity == this.index) {
                    System.out.println("满了，等待非满信号");
                    notFull.await();
                }
                list.add(e);
                index++;
                System.out.println("当前有："+index);
                //唤醒一个，等待非空信号量的线程
                notEmpty.signal();
            } catch (InterruptedException ex) {
                //若在等待期间
                notFull.signal();
                ex.printStackTrace();
            }finally {
                lock.unlock();
            }
        }


        /**
         * 获取操作，判断容器是否为空，为空则等待非空信号量
         * 获取数据后，释放非满信号量唤醒相应线程
         * @return
         * @throws InterruptedException
         */
        public int get()throws InterruptedException{
            this.lock.lockInterruptibly();
            try{
                while (this.index == 0){
                    System.out.println("空了，等待非空信号！");
                    this.notEmpty.await();
                }
               this.notFull.signal();
            }catch (InterruptedException ex){
                notEmpty.signal();
                ex.printStackTrace();
            }finally {
                lock.unlock();
            }
            System.out.println("消费元素下标："+ --index);
            return this.list.remove(index);
        }
    }
}
