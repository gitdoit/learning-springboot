package org.seefly.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 刘建鑫
 */
public class BlockingQueueDemo1 {

	public static void main(String[] args) {

		BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
		AtomicInteger count =  new  AtomicInteger(0); 
		P p = new P(queue,count);
		V v = new V(queue);
		
		Thread t = new Thread(p);
		Thread t2 = new Thread(v);
		Thread t3 = new Thread(p);
		t.start();
		t2.start();
		//t3.start();
		try {
			Thread.sleep(3000);
			p.stop();//调用这个方法，三个线程可能也不会停止，因为他们可能因为put和take方法使线程阻塞了，而消费这线程不在消费，生产者线程不在生产
			v.stop();//所以出现了死锁。
			Thread.sleep(1000);
			System.out.println("t:"+t.isAlive()+"t2:"+t2.isAlive()+"t3:"+t3.isAlive());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class P implements Runnable{
	private BlockingQueue<String> queue;
	private AtomicInteger count;
    /**
     * 使用i会造成读脏数据
     */
	private int i = 0;
    /**
     * 使用volatile关键字定义的数据，会使数据更改成原子操作，数据读写成排他性。不会出现读脏现象
     */
	private volatile int id = 0;
	boolean flag = true;
	public P(BlockingQueue queue,AtomicInteger count) {
		this.queue = queue;
		this.count = count;
	}
	
	@Override
	public void run() {
		String data;
		try {
			System.out.println("启动生产者线程");
			while(flag) {
				//data = "产品"+i++;若使用 i 则在多线程环境下会造成读脏数据
                //使用此方法则不会在会使自增操作成原子操作。不会造成读脏数据
				data = "产品" + count.incrementAndGet();
				//data = "产品" + id++;多个生产者线程对id进行更改，则会出现排他现象。不会出现都脏数据
				queue.put(data);
				System.out.println("生产了产品"+data+"，仓库中有"+queue.size());
				Thread.sleep(200);
			}
		}
		catch(InterruptedException e) {
			e.printStackTrace();
			}
	}
	public void stop() {
		this.flag = false;
	}
}

class V implements Runnable{
	private BlockingQueue<String> queue;
	private boolean flag = true;

	public V(BlockingQueue queue) {
		this.queue = queue;
	}
	@Override
	public void run() {
		System.out.println("消费者线程启动");
		String data;
		try {
			while(flag) {
				data = queue.take();
				Thread.sleep(200);
				System.out.println("消费产品"+data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void stop() {
		this.flag = false;
	}
}