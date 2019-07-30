package org.seefly.thread.lev1;


import lombok.Data;

/**
 * 本类的目的是演示多个线程并发执行时的问题。
 * 有两个生产者以及两个消费者，我们的目的是生产一个消费一个。
 * 所以通过flag标志位确定生产与消费交替实现。
 *
 * 如果生产者或消费者中的while 和 notifyAll 换成 if 和 notify 会出现如下现象。
 * 如果用if判断flag标志位，那么在同步函数内，t1开始执行获得资源判断flag为假，跳过wait，执行后面语句。并在时间片用光之前又执行到了if，此时flag为真
 * t1等待且释放锁。t2获得执行资源，判断flag也为真，也等待，释放锁。t3获得资源判断 ！flag 为假，跳过wait。
 * 执行消费。并唤醒最开始进入线程池的t1（并不代表t1有执行资源，要抢）。然后t3有执行了一次
 * 到if的时候判断flag并进入等待，释放资格。正好被t4抢到。t4一判断也进入等到放弃资格。此时只有t1活着，它运行之后生产了一个商品。
 * 唤醒了t2。之后又来一次判断并进入了等待。只有t2活着，拿到了资源。
 * 因为等待之前已经判断过flag，所以直接向下执行，也生产了一个商品。所以这就出现了连续生产两个商品的现象。
 *
 * @author 刘建鑫
 * */
public class B5_NotifyAll {

	public static void main(String[] args){
	    final Resource res = new Resource("商品");

		new Thread(()->{while (true){res.production();} },"生产线1号").start();
		new Thread(()->{while (true){res.production();}},"生产线2号").start();

		new Thread(()->{while (true){res.consume();}},"消费者1号").start();
		new Thread(()->{while (true){res.consume();}},"消费者2号").start();

	}


    /**
     * 共享资源
     */
    @Data
	private static class Resource{
        private String name;
        private int count = 0;
        private boolean flag = false;

        public Resource(String name){
            this.name = name;
        }

        /**
         * 生产方法，根据标志位判断商品是否被消费掉了。
         * 若没有被消费则等待
         */
        public synchronized void production(){
            while(flag){
                try{this.wait();} catch(Exception ex){}
            }
            System.out.println(Thread.currentThread().getName()+" 生产了一个" + name + "，编号为：" + count++);
            this.flag = true;
            this.notifyAll();
        }

        /**
         * 消费者方法，根据标志位判断是否有商品可以被消费
         * 若没有可消费的商品则等待
         */
        public synchronized void consume(){
            while(!flag){
                try{this.wait();} catch(Exception ex){}
            }
            System.out.println(Thread.currentThread().getName()+" 消费了一个" + name + "，编号为：" + count);
            this.flag = false;
            this.notifyAll();

        }

    }
}




