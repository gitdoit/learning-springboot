package org.seefly.thread;

/**
 * @author 刘建鑫
 */
public class A4_ThreadGroup {

	public static void main(String[] args) {
		// 新建线程组，指定其父线程组为主线程组。
		// 即使出现了线程未捕获异常被线程组的uncaughtException捕获了，那么出现异常的线程也会停止。将异常抛向上级
        // 如果定义了线程组的异常捕获器，那么全局的异常捕获将不会对此线程组起作用
		ThreadGroup g = new ThreadGroup(Thread.currentThread().getThreadGroup(),"MyThreadGroup") {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("线程组g的未捕获异常处理器,异常线程为"+t.getName()+"  "+e);

				//从这里抛出的异常将不被任何程序捕获
				//throw new RuntimeException("线程组自定义异常捕获抛出来的异常！");
                //int i = 2 / 0;
			}
		};

		// 该线程组没有定义自己的异常捕获器，父线程组的异常捕获器（全局的异常捕获器）将会捕获从该线程组中抛出的异常
        ThreadGroup g2 = new ThreadGroup(Thread.currentThread().getThreadGroup(),"MyThreadGroup2");
        new Thread(g2,() ->{
            int i = 1 / 0;
        },"t2").start();

        //设置是否为后台线程组，组内所有线程皆受改变
		g.setDaemon(true);
		
		// 创建线程，指定其所属线程组以及名字
		Thread t1 = new Thread(g,() ->{
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                throw  new RuntimeException("sleep的时候被中断了！！！");
            }
        } ,"t1");
		
		Thread t2 = new Thread(g,() ->{
            while(true){
            }
        },"t2");


		//将会捕获所有的从线程中抛出的异常
		Thread.setDefaultUncaughtExceptionHandler((thread,throwable) ->
                System.out.println("全局线程异常捕获："+thread+"====="+throwable)
        );

		t1.start();
		t2.start();
		ThreadGroup g1 = Thread.currentThread().getThreadGroup();
		System.out.println("主线程组名称:"+g1.getName()+"是否为后台线程"+g1.isDaemon());
		System.out.println("自定义线程组名称:"+g.getName()+"是否为后台线程"+g.isDaemon());
		System.out.println("线程组："+g.getName()+"活动的线程数为"+g.activeCount());
        //如果线程内有被阻塞的线程，那么调用该方法，会让被阻塞的线程抛出异常导致该线程结束。
        //如果线程组内所有线程都没有被阻塞，那么调用此方法将不起任何作用
		g.interrupt();
		try {
			Thread.sleep(30);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("调用interrupt之后活动的线程数为:"+g.activeCount());
	}
}

