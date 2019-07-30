package org.seefly.thread.lev1;

import org.junit.Test;

/**
 * 该类演示了对于同一个同步代码块,使用不同的锁会产生不同的效果。
 * @author 刘建鑫
 * */
public class B2_SynBlock {

    /**
     * 如果多个线程使用不同的锁执行相同的同步代码块，那么多个线程会并发执行这个同步代码块
     */
	@Test
	public void testDiffrentBlock(){
        Runnable1 p = new Runnable1();
        Thread t1 = new Thread(p,"t1");
        Thread t2 = new Thread(p,"t2");
        t1.start();
        t2.start();
    }

	private static class Runnable1 implements Runnable{
        private int count = 100;
        @Override
        public void run(){
            //每一个线程进来都会新建一个对象，每个线程的锁并不一样
            Object a = new Object();
            synchronized(a){
                while(count > 0){
                    try{Thread.sleep(10);}catch(Exception ex){}
                    System.out.println(Thread.currentThread().getName()+ "   ******   "+count--);
                }
            }
        }
    }







    /**
     * 对于不同的同步代码块，如果使用相同的锁，那么即使有多个线程他们也不会并发执行。
     */
    @Test
    public void testSameBlock(){
        Runnable2 p = new Runnable2();
        Thread t1 = new Thread(p,"t1");
        Thread t2 = new Thread(p,"t2");
        t1.start();
        t2.start();
    }

    private static class Runnable2 implements Runnable{
        private int a = 100;
        private int b = 100;
        Object c = new Object();
        @Override
        public void run() {
            if (Thread.currentThread().getName().compareTo("t1") == 0) {
                //使用this关键字代表调用者本身，所以每个线程使用的都是同一把锁
                synchronized (this) {
                    while (a > 0) {
                        try {
                            Thread.sleep(10);
                        } catch (Exception ex) {
                        }
                        System.out.println(Thread.currentThread().getName() + "   ******   " + a--);
                    }
                }
            } else {
                synchronized (this) {
                    while (b > 0) {
                        try {
                            Thread.sleep(10);
                        } catch (Exception ex) {
                        }
                        System.out.println(Thread.currentThread().getName() + "   ******   " + b--);
                    }
                }
            }
        }
	}


}


