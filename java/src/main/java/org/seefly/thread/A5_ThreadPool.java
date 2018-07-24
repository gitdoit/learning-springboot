package org.seefly.thread;

import java.util.concurrent.*;

/**
 * @author 刘建鑫
 * java通过Executors提供四种线程池
 * newCacheThreadPool创建一个可缓存的线程池，容量可灵活更改，若容量不够处理需要，则新开辟线程。若大于处理需要则回收。
 * newFixedThreadPool创建一个容量固定的线程池，可以限制线程最大并发量，超出的线程在队列中等待。
 * newScheduledThreadPool 创建一个容量固定的线程池，支持定时以及周期处理任务
 * newSingleThreadExecutors创建容量为1的线程池，使用这唯一的线程处理任务，保证所有任务按指定顺序执行任务(FIFO,LIFO,优先级)
 * http://cuisuqiang.iteye.com/blog/2019372
 * */
public class A5_ThreadPool {
	public static void main(String[] args) {
		// 循环创建线程，被FixedThreadPool包装。线程池容量为3，所以同时并发的线程最多只有三个，其余的等待
        // FixedThreadPool.shutdown();此方法会使线程停止接受新任务(若继续添加则会报错)，且完成正在执行与等待列队中的任务。
        // shutdownNow会使正在执行的线程处于中断状态，返回并清除等待列队中的线程
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        // 创建缓冲线程池
        ExecutorService CachePool = Executors.newCachedThreadPool();
		for(int i = 0; i < 100; i++) {
			fixedThreadPool.execute(() -> {
                System.out.println("固定线程池："+Thread.currentThread().getName()+" is running");
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            });
            CachePool.execute(() -> {
                System.out.println("缓冲线程池："+Thread.currentThread().getName()+" is running");
            });
		}
		
	}

}


