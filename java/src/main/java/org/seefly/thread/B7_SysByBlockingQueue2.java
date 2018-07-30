package org.seefly.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 刘建鑫
 */
public class B7_SysByBlockingQueue2 {

    public static void main(String[] args) {
        final BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        final AtomicInteger count = new AtomicInteger(0);


            Thread t = new Thread(() -> {
                System.out.println("启动生产者线程");
                try {
                    while (true) {
                        //使用此方法则不会在会使自增操作成原子操作,不会在多线程环境下会造成读脏数据
                        String data = "产品" + count.incrementAndGet();
                        queue.put(data);
                        System.out.println("生产了产品" + data + "，仓库中有" + queue.size());
                        Thread.sleep(200);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

            });
            Thread t1 = new Thread(() -> {
                System.out.println("消费者线程启动");
                String data;
                try {
                    data = queue.take();
                    Thread.sleep(200);
                    System.out.println("消费产品" + data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            t.start();
            t1.start();

        }

    }
