package org.seefly.thread.lev1;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
/**
 * 泛型接口Callable<T> 与Runnable接口类似，通过重写其中call方法来使线程执行其中内容
 * 此方法与run方法不同，它具有返回值，返回值类型与泛型一致
 * 但是此接口的实现实例不能直接放进Thread中去执行，因为它不是Runnable接口的子接口。
 * FutureTask类实现了Future接口，与Runnable接口。并且Future接口可以控制call方法。
 * 所以我们是Callable的实现实例放入FutureTask的实例中，通过FutureTask可以操作call方法。
 * 再将FutureTask的实例放进Thread中去启动。这样看起来callab接口像是runnable的增强
 * FutureTaks中有几个方法：
 * boolean cancel(boolean mayInterruptIfRunning) 取消正在执行的callab任务
 * V get() 获取callable中call的返回值，只有在call运行完毕后才会得到返回值，所以会引起调用此方法的线程阻塞等待
 * V get(long tiemout,timeUnti unit) 获取call方法的返回值，但是若在tiemou和unit指定时间内没有返回值，则会报错TimeOutException
 * boolean isCancelled() 如果在任务完成前被取消了，则返回true
 * boolean isDone() 如果任务完成则返回true
 * @author 刘建鑫
 * */
public class A2_FutureTask implements Callable<Integer>{

    /**
     * 复写其中call方法，注意返回值类型要与泛型定义的一致
     * @return Integer
     */
	@Override
	public Integer call() {
		int i = 0;
		for(; i < 100; i++) {
			System.out.println(Thread.currentThread().getName() + ":i = "+ i);
		}
		return i;
	}


	public static void main(String[] args) {
		//实例化callable的实现类
		A2_FutureTask f = new A2_FutureTask();
        //使用FutureTask对象对其进行包装
		FutureTask<Integer> task = new FutureTask<>(f);
        //再使FutureTaks实例作为Thread的target，并启动
		new Thread(task).start();
		try {
		    //get方法会抛出异常
            //通过控制FutureTask的实例对call进行操作
			System.out.println(task.get());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

