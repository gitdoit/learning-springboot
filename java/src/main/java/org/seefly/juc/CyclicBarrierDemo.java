package org.seefly.juc;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 循环栅栏，跟{@link CountDownLatch}比起来，这个类有些类似。
 * 但是那个东西只能倒计时一次，而这个可以循环。
 *
 * 例如一个任务必须要10个线程一组来执行，而这10个线程可能会在不同的时间被创建，这时
 * 就可以使用这个东西了。
 *
 * 构造方法
 * {@link CyclicBarrier#CyclicBarrier(int, java.lang.Runnable)}
 * 第一个参数指定循环大小，第二个指定在一个循环完成后做的动作。
 *
 * 该类模拟了10个士兵为一组，集结完毕后一起执行任务，所有所有士兵的任务都执行完毕后，再通知司令。
 * 代码来自《实战JAVA高并发程序设计》
 * @author liujianxin
 * @date 2018-11-21 10:29
 */
public class CyclicBarrierDemo {
    public static class Soldier implements  Runnable{
        private String soldier;
        private final CyclicBarrier cyclic;

        Soldier(CyclicBarrier cyclic,String soldierName){
            this.cyclic = cyclic;
            this.soldier = soldierName;
        }

        @Override
        public void run() {
            try{
                cyclic.await();
                doWork();
                cyclic.await();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        void doWork(){
            try{
                Thread.sleep(RandomUtils.nextLong(0,5000));
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(soldier+":任务完成！");
        }
    }

    public static class BarrierRun implements Runnable{
        boolean flag;
        int N;

        public BarrierRun(boolean flag,int N){
            this.flag = flag;
            this.N = N;
        }

        @Override
        public void run() {
            if(flag){
                System.out.println("司令:干得漂亮");
            }else {
                System.out.println("集结完毕");
                flag = true;
            }
        }
    }

    public static void main(String[] args){
        final  int N = 10;
        Thread[] soldiers = new Thread[N];
        boolean flag = false;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(N,new BarrierRun(flag,N));
        System.out.println("队伍集合！");
        for(int i = 0 ;i < N; i++){
            System.out.println("士兵"+i+"报道！");
            soldiers[i] = new Thread(new Soldier(cyclicBarrier,"士兵"+i));
            soldiers[i].start();
        }
     }
}
