package org.seefly.thread;

import java.util.concurrent.*;

/**
 * @author 刘建鑫
 * 阻塞列队的作用是在多线程环境下起到同步的目的。
 * 多个线程对一个阻塞列队进行操作，使用BlockingQueue.put()方法，若队列已满，则阻塞该线程，等不满的时候会自动启动
 *
 * BlockingQueue<T>是一个接口。接口中含有下列方法
 *
 *
 * boolean offer(T:e)/(T:e,long , TimeUnit)
 * 向阻塞列队的队尾中放入e，如果 队列 已满，那么将返回false！如果队列已满那么在int * TimeUnit时间之后还是满的将返回false。
 * 此方法会使线程在该处停留等待long * TimeUnit长度的时间。
 *
 * T poll()/(long,TimeUnit)
 * 从阻塞列队头部取出元素，如果没有则返回null。如果在long * TimeUnit时间之后还没有，那么将返回null。
 * 此方法会使线程在该处停留等待long * TimeUnit
 *
 * T peek()
 * 从阻塞列队中取出头部元素，且不删除
 *
 * boolean add(T:e)
 * 向阻塞列队尾插入元素，如果队列已满，那么将抛出异常.
 *
 * T remove()
 * 从阻塞列队头部取出，并删除该元素。如果队列为空，那么会抛出异常。
 *
 * boolean remove(T:e)
 * 从阻塞列队中取出该元素，如果没有找到这个元素，那么返回false。否则返回true
 *
 * T element()
 * 从阻塞列队头部取元素，且不删除。若队列为空，则抛出异常
 *
 * void put(T:e)
 * 向阻塞列队尾部插入元素，如果队列已经满了，那么调用此方法的线程将会进入阻塞态，直到队列有空位。
 * T take()
 * 从阻塞列队头部取元素，如果队列为空，那么调用此方法的线程将会进入阻塞态，知道队列中有元素。
 *
 *
 * */
public class B7_SysByBlockingQueue {

	public static void main(String[] args) {
	    //使用阻塞队列模拟临界区资源
		BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);

        ExecutorService doubleThreadPool = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), new ThreadPoolExecutor.AbortPolicy());

        doubleThreadPool.execute(()-> {
            boolean isRunning = true;
            boolean isInsert;
            System.out.println("启动生产者线程！");
            try {
                while(isRunning) {
                    System.out.println("正在生产商品");
                    Thread.sleep(Math.round(1000));
                    //若两秒之后消费者还没消费商品，那么退出
                    isInsert = queue.offer("productiong",2, TimeUnit.SECONDS);
                    if(!isInsert) {
                        isRunning = false;
                        System.out.println("退出生产者线程");
                    }
                }
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        });

        doubleThreadPool.execute(() ->{
            String isGet;
            boolean isRunning = true;
            try {
                while(isRunning) {
                    System.out.println("正在取出产品");
                    Thread.sleep(Math.round(1000));
                    //get the goods
                    isGet = queue.poll(2,TimeUnit.SECONDS);
                    if(isGet != null) {
                        System.out.println("正在消费产品--"+isGet);
                    }
                    else{
                        //若两秒之后还没有商品被生产，那么则退出线程。
                        isRunning = false;
                    }
                }
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        });
        doubleThreadPool.shutdown();
	}

}
