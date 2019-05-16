package org.seefly.springbasic.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author liujianxin
 * @date 2018-11-30 10:48
 */
//@Component
public class ScheduledDemo {
    @Autowired
    private AsyncTaskDemo asyncTaskDemo;

    /**
     * 基本操作
     */
    //@Scheduled(fixedDelay = 3 * 1000)
    public void scheduled1(){
        String threadName = Thread.currentThread().getName();
        int thredPri = Thread.currentThread().getPriority();
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.currentThread().getUncaughtExceptionHandler();
        System.out.println("threadName:"+threadName +"|threadPriority:"+thredPri+"|threadGroup:"+threadGroup +"|uncaughtExceptionHandler:"+uncaughtExceptionHandler);
    }

    /**
     * 定时任务，调用异步执行方法
     */
    @Scheduled(fixedDelay = 3 * 1000)
    public void scheduled2(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //调用异步方法是立即返回的，可以看到，异步方法休眠了2秒，这里察觉不到。
        asyncTaskDemo.asyncWork(Thread.currentThread().getName());
        stopWatch.stop();
        System.out.println("调用异步完毕，耗时："+stopWatch.getTotalTimeMillis());
    }

    /**
     * 演示异步方法抛出异常
     */
    //@Scheduled(fixedDelay = 3 * 1000)
    public void callException(){
        asyncTaskDemo.runWithException();
    }

    /**
     * 演示调用有返回值的异步方法
     * @throws ExecutionException if this future completed exceptionally
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    @Scheduled(fixedDelay = 3 * 1000)
    public void callWithResult() throws ExecutionException, InterruptedException {
        CompletableFuture future = asyncTaskDemo.returnWithFuture();
        // 查看结果是否产生
        System.out.println(future.isDone());
        // 没有产生，自旋
        while (!future.isDone()){}
        System.out.println(future.get());
    }
}
