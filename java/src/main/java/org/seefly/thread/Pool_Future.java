package org.seefly.thread;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Pool_Future {

	public static void main(String[] args) throws Exception, ExecutionException {
		ArrayList<Future> list = new ArrayList<>();
		ExecutorService pool = Executors.newFixedThreadPool(5);
		
		FutureTask<Integer> task = new FutureTask(new MyCall());
//		list.add(pool.submit(task));
//		list.add(pool.submit(new MyCall()));
//		for(Future e : list) {
//			System.out.println(e.get());
//		}
		System.out.println(pool.submit(task).get());
		pool.shutdown();
	}

}

class MyCall implements Callable<Integer>{
	@Override
	public Integer call() throws Exception {
		return 5;
	}
	
}