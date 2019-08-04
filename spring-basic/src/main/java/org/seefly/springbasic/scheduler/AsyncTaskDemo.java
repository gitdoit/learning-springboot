package org.seefly.springbasic.scheduler;

import org.seefly.springbasic.utils.ThreadUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

/**
 * 将方法标记为异步执行候选的注释，也可以在类型级别使用，在这种情况下，所有类型的方法都被视为异步。
 * 就目标方法签名而言，支持任何参数类型。但是，返回类型被约束为void或java.util.concurrent.Future。
 * 在后一种情况下，您可以声明更具体的{@link ListenableFuture}或{@link CompletableFuture}类型
 * 这些类型允许与异步任务进行更丰富的交互，并通过进一步的处理步骤立即进行组合。
 * 从代理返回的Future句柄将是一个实际的异步Future，可用于跟踪异步方法执行的结果。
 * 但是，由于目标方法需要实现相同的签名因此必须返回一个临时的Future句柄，该句柄只传递一个值：Spring的AsyncResult，EJB 3.1的javax.ejb.AsyncResult，
 * 或java.util.concurrent.CompletableFuture.completedFuture（Object）。
 * @author liujianxin
 * @date 2018-11-30 14:19
 */
@Component
public class AsyncTaskDemo {

    @Async
    public void asyncWork(String callerName){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("调用者线程名："+callerName+"|执行任务线程名："+Thread.currentThread().getName());
    }

    /**
     * 演示异步方法抛出异常
     */
    @Async
    public void runWithException(){
        throw new RuntimeException("CAFFE LATTE");
    }

    /**
     * 演示有返回值的异步调用
     * {@link CompletableFuture}的使用方法详见本项目java模块下concurrent.CompletableFutureDemo.java
     * @return 返回一个java.util.concurrent.Future的实现类，这预示着结果可能在将来的任意时间得到。
     */
    @Async
    public CompletableFuture returnWithFuture(){
        CompletableFuture<String> caffe = CompletableFuture.completedFuture("CAFFE").thenApplyAsync(p -> {
            ThreadUtil.randomSleep();
            return p + " LATTE";
        });
        //java.util.concurrent.Future
        return caffe;
    }

    public void testInvokeBySelf(){
    }
}


