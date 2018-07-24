package org.seefly.thread;
/**
 * 该类用来演示synchronized中锁的问题
 * 
 * Ticket1类继承Runnable接口，添加一个属性代表票数。覆写run方法。
 * 方法内有一个同步代码块和同步函数，通过更改flag值使线程执行不同的同步代码
 * 
 * 值得注意的是synchronized锁的并不是代码，之前一直以为synchronized是将块内代码整合成一条语句执行来达到同步的目的，像是数据库中事物的原子性概念。
 * 但是事实情况并不是这个样子，事实情况是cup分配给这个线程运行资源后，这个线程如果在一个时间片内没有执行
 * 完同步代码块内的内容，时间片用完之后它还是会被换出。那么既然如此它又是怎么实现同步的呢？
 * 其实每一个线程在执行同步代码块或同步函数的时候都会通过synchronized的参数（一个对象）来判断自己是否有资格执行其中代码。而这个对象就相当于锁。
 * 而每个对象只具有一把锁，例如有两个线程执行同
 * 一个Runnable实例中的run方法，而run方法中有一个synchronized同步块，其参数为this即Runnable实例本身
 * 线程A率先得到运行资源，执行run方法，碰到其中的同步代码块，首先检查synchronized的参数（this）对于该同步代码块的锁是否被取走。如果没有取走则A获得this对于该
 * 同步代码块的锁。然后执行其中内容，一段时间后时间片用完，但是却没有执行完同步块中的内容。
 * 此时，线程A继续持有this对于该同步块的锁而并不会释放。而线程B获得运行资源之后也像线程A一样检查是否有执行资格，发现没有则等待，直到时间片用完也不会执行。
 * 
 * 可以将synchronized的参数理解为一个载体，这个载体具有锁的性质。若同一个同步代码块却有不同的锁，那么也会出现多线程并发执行该代码块的情况。只有当这多个线程
 * 使用对于这个同步代码块的同一把锁的时候才会达到同步的目的。
 * 
 * 
 * 当同步函数被静态之后那么它的锁就不在是this，因为在静态方法进入内存时还没有生成对象，更没有这个对象锁。所以它使用的是 类名.class对象作为他的锁。
 * 
 * */
public class B1_SynBasic {
	public static void main(String[] args) {
		Ticket1 p = new Ticket1();
		//Ticket1 p1 = new Ticket1();
		Thread t1 = new Thread(p,"t1");
		Thread t2 = new Thread(p,"t2");
		
		t1.start();
		try{
		    Thread.sleep(100);
		}catch(Exception ex){}
		p.flag = false;
		t2.start();

		try{
		    Thread.sleep(1300);
		}catch(Exception ex){}
		t1.stop();
		t2.stop();
	}

	private static class Ticket1 implements Runnable{
        private int tick = 100;
        boolean flag = true;
        @Override
        public void run(){
            if(flag){
                while(true){
                    synchronized(this){
                        if(tick > 0){
                            try{Thread.sleep(10);}catch(Exception ex){}
                            System.out.println(Thread.currentThread().getName()+"....code:"+tick--);
                        }
                    }
                }
            }
            else{
                while(true){
                    show();
                }
            }
        }
        public synchronized void show(){
            {
                if(tick > 0){
                    try{Thread.sleep(10);}catch(Exception ex){}
                    System.out.println(Thread.currentThread().getName()+"******shou:"+tick--);
                }
            }
        }
    }

}

