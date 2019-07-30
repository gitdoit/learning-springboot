package org.seefly.thread.lev1;
/**
 * 后台线程又称为守护线程
 * 该线程有一个特点那就是，在主线程死亡后那么它也会死亡。
 * 例如有两个线程t1,t2
 * 在t1线程中new出t2线程，并在调用start方法前
 * 设置：t2.setDaemon(true) 然后t2.start()
 * 此时t2线程就被设置为t1线程的守护线程，在t1死亡后t2也会死亡
 * 注意不要在线程为就绪态后设置Daemon否则会报IllegalThreadStateException
 *
 * @author 刘建鑫
 * */
public class A3_DeamonThread implements Runnable{
	public static void main(String[] args) {
		A3_DeamonThread r = new A3_DeamonThread();
		Thread t = new Thread(r,"t1");
		t.start();
	}

    /**
     * 由前台线程创建的线程都为前台线程，反之都为后台线程
     */
	@Override
	public void run() {
		Thread t2 = new Thread(() ->{
            for(int i = 0; i < 1000; i++){
                //设置循环1000次，比t1线程多
                System.out.println(Thread.currentThread().getName()+":t2="+i);
            }
        },"t2");
        //设置为后台线程
		t2.setDaemon(true);
        //启动
		t2.start();
		for(int t1 = 0;t1 < 100; t1++){
            //t1线程循环100次
			System.out.println(Thread.currentThread().getName()+"t1:"+t1);
        }
	}
	
}

