package org.seefly.thread;
/**
 * 该类展示了Thread无法共享的缺点。
 * 实例A,C是Thread的子类，他们都在买票，但是剩余票数无法共享。
 * 而Runnable实例B虽然开启了两个线程，但是他们却卖的同一张票。
 * 以后要使用Runnable创建线程
 *
 * 实现共享的方法。
 * 定义类实现Runnable接口，或者直接构建Runnable接口
 * 覆写其中run方法
 * 通过Thread类建立线程对象
 * 将Runnable接口的对象作为实参构建Thread类对象
 * 调用Thread类的start方法开启方法，并调用Runnable接口的run方法
 * @author 刘建鑫
 * */
public class A1_Thread {

	public static void main(String[] args) {
        //Thread对象资源不共享
		Thr c = new Thr(5);
		Thr a = new Thr(5);
		c.start();
		a.start();

        //创建Runnable对象，然后用作实参放进Thread对象中、可以实现共享
		Runn b = new Runn(5);
		new Thread(b).start();
		new Thread(b).start();
		
	
		System.out.println(Thread.currentThread().getName());

	}

	private static class Thr extends Thread{
        private int count;

        public Thr(){
            count = 0;
        }
        public Thr(int count){
            this.count = count;
        }
        @Override
        public void run(){
            while(count > 0){
                System.out.printf("Thread��ʣ%d��Ʊ\n",count);
                System.out.println(Thread.currentThread().getName());

                count--;
            }
        }
    }

    private static class Runn implements Runnable{
        private int count = 0;
        public Runn(){
            count = 0;
        }
        public Runn(int count){
            this.count = count;
        }

        @Override
        public void run(){
            while(count > 0){
                System.out.printf("Runnable还剩%d张票\\n",count);
                count--;
            }
        }
    }
}






