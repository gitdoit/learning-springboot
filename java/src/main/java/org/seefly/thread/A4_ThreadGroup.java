package org.seefly.thread;

/**
 *
 * 该类用来演示线程组以及线程组的未捕获异常处理器
 * 对于一个处于组线程组中的线程，如果它抛出异常将会被组线程组的未捕获异常处理器
 * 前提是你要提前设置组线程组的为捕获异常处理器；
 * 对于自定义的线程组，设置器父线程组为主线程组，再同时为他们两个都设置未捕获异常处理器
 * 那么在自定义线程组中的线程抛出的异常将会被自定义线程组中的为捕获异常处理器捕获，不会被父线程组
 * 中的为捕获异常处理器捕获，并且在自定义线程组中的为捕获异常处理器抛出的异常将不会在任何地方被捕获，且不会打印异常信息
 * @author 刘建鑫
 */
public class A4_ThreadGroup {

	public static void main(String[] args) {
        //设置主线程组为父线程组，不设置未捕获异常处理器
        ThreadGroup noUncaughtException = new ThreadGroup(Thread.currentThread().getThreadGroup(),"MyThreadGroup2");
        new Thread(noUncaughtException,() ->{
            int i = 1 / 0;
        },"t2").start();


        //设置主线程组为父线程组，设置未捕获异常处理器
		ThreadGroup hasUncaughtException = new ThreadGroup(Thread.currentThread().getThreadGroup(),"MyThreadGroup") {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("自定义线程组异常捕获："+t.getName()+"  "+e);
			}
		};
		//设置为守护线程组，组内所有线程都会改变
        hasUncaughtException.setDaemon(true);
		Thread t1 = new Thread(hasUncaughtException,() ->{
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                throw  new RuntimeException("sleep??????ж????????");
            }
        } ,"t1");
		
		Thread t2 = new Thread(hasUncaughtException,() ->{
            while(true){
            }
        },"t2");
		Thread.setDefaultUncaughtExceptionHandler((thread,throwable) ->
                System.out.println("主线程组为捕获异常处理器："+thread+"====="+throwable)
        );

		t1.start();
		t2.start();
		ThreadGroup g1 = Thread.currentThread().getThreadGroup();
		System.out.println("主线程组："+g1.getName()+"，是否为守护线程组"+g1.isDaemon());
		System.out.println("自定义线程组："+hasUncaughtException.getName()+"是否为守护线程组:"+hasUncaughtException.isDaemon()+"组内活动线程数量："+hasUncaughtException.activeCount());
        //中断方法会将组内所有的线程中断状态置为true
        hasUncaughtException.interrupt();
		try {
			Thread.sleep(30);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("调用线程组中断方法之后活动的线程数量为:"+hasUncaughtException.activeCount());
	}
}

