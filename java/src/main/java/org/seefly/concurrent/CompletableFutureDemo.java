package org.seefly.concurrent;

import com.sun.xml.internal.stream.events.CommentEvent;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

/**
 * 超大型工具类
 * @author liujianxin
 * @date 2018-11-23 11:13
 */
public class CompletableFutureDemo {


    private static void randomSleep(){
        try {
            Thread.sleep(RandomUtils.nextLong(1000,3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public  void completed1(){
        //返回一个已经计算完成的CompletableFuture
        CompletableFuture<String> cf = CompletableFuture.completedFuture("value");
        System.out.println(cf.isDone());
        System.out.println(cf.getNow(null));
    }

    /**
     * 运行一个简单的异步阶段
     */
    @Test
    public void completed2() throws InterruptedException {
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
            // 异步线程为守护线程
            assert Thread.currentThread().isDaemon();
            randomSleep();
        });
        // 主线程检查是否计算完毕
        System.out.println(cf.isDone());
        Thread.sleep(2000);
        System.out.println(cf.isDone());
    }

    @Test
    public void completed3(){
        // 将小写转大写
        CompletableFuture<String> cf = CompletableFuture.completedFuture("java8").thenApply(String::toUpperCase);
        System.out.println(cf.isDone());
        //get  getNow在计算未完成前会被阻塞
        System.out.println(cf.getNow("Not yet"));
    }

    /**
     * 演示异步执行计算
     */
    @Test
    public void completed4(){
        CompletableFuture<String> cf = CompletableFuture.completedFuture("java8").thenApplyAsync(s -> {
            randomSleep();
            return s.toUpperCase();
        });
        // 异步没有计算完成，返回默认
        System.out.println(cf.getNow("Not yet!"));
        // 阻塞等待计算结果
        System.out.println(cf.join());
    }

    /**
     * 自定义线程池，提供线程
     */
    @Test
    public void completed5(){
        LongAdder la = new LongAdder();
        //自定义线程池
        @SuppressWarnings("AlibabaThreadPoolCreation") ExecutorService service = Executors.newFixedThreadPool(3, r -> {
            la.increment();
            return new Thread(r, "custom-executor-" + la.intValue());
        });
        // 使用自定义线程池
        CompletableFuture<String> cf = CompletableFuture.completedFuture("java8").thenApplyAsync((p) -> {
            // 断言是否使用了自定义线程池创建线程
            assert Thread.currentThread().getName().startsWith("custom-executor");
            // 使用了自定义线程池创建的线程，不再为守护线程
            assert !Thread.currentThread().isDaemon();
            randomSleep();
            return p.toUpperCase();
        }, service);

        assert Objects.isNull(cf.getNow(null));
        assert "JAVA8".equals(cf.join());
    }

    /**
     * 演示消费数据，不产出结果（副作用）
     */
    @Test
    public void completed6(){
        StringBuilder sb = new StringBuilder();
        CompletableFuture.completedFuture("java8").thenAccept(sb::append);
        System.out.println(sb.toString());
    }

    /**
     * 演示异步消费数据
     */
    @Test
    public void completed7(){
        StringBuilder sb = new StringBuilder();
        CompletableFuture.completedFuture("java8").thenAcceptAsync(sb::append).join();
        System.out.println(sb.toString());
    }

    /**
     * 演示异步计算时的异常处理
     */
    @Test
    public void complete8(){
        // 首先异步转化
        CompletableFuture<String> cf = CompletableFuture.completedFuture("java8").thenApplyAsync(s ->{
            randomSleep();
            return s.toUpperCase();
        });
        // 设置出现异常时的处理阶段
        CompletableFuture exceptionHandler = cf.handle((result, exception) -> (exception != null) ? "message upon cancel" : "");
        // 若未完成，则抛出异常，阶段置为已完成状态（这里由于randomSleep()，会抛出异常）
        cf.completeExceptionally(new RuntimeException("completed exceptionally"));
        // 以任何方式（异常，计算完成等）完成都会返回true
        assertTrue("Was not completed exceptionally", cf.isCompletedExceptionally());
        try {
            // 上面已由于调用completeExceptionally导致异步计算异常，这里再调用这个方法会导致当前线程抛出异常
            cf.join();
            fail("Should have thrown an exception");
        } catch(CompletionException ex) { // just for testing
            assertEquals("completed exceptionally", ex.getCause().getMessage());
        }
        assertEquals("message upon cancel", exceptionHandler.join());
    }

    /**
     * 演示异步处理时的异常处理
     */
    @Test
    public void completed9(){
        CompletableFuture<String> fc = CompletableFuture.completedFuture("java8").thenApplyAsync(s -> {
            randomSleep();
            return s.toUpperCase();
        });
        // 若fc正常完成，则fc2也会以相同的值正常完成。否则，fc2触发自己的计算阶段，并返回值。
        CompletableFuture<String> fc2 = fc.exceptionally(throwable -> "canceled message");
        // 取消，导致 CompletionException，fc2依赖也会因此完成。
        assertTrue("Was not canceled",fc.cancel(true));
        assertTrue("Was not completed exceptionally",fc.isCompletedExceptionally());
        assertEquals("canceled message",fc2.join());
    }


    /**
     * 谁先完成？
     */
    @Test
    public void completed10(){
        String msg = "java8";
        CompletableFuture<String> fc = CompletableFuture.completedFuture(msg).thenApplyAsync(s -> {
            randomSleep();
            return s.toUpperCase();
        });
        CompletableFuture<String> fc1 = CompletableFuture.completedFuture(msg).thenApplyAsync(s -> {
            randomSleep();
            return s.toLowerCase();
        });
        // 将fc 和 fc1 组合，谁先完成用谁的结果进行计算
        CompletableFuture<String> compose = fc.applyToEither(fc1, s -> s + " from applyToEither");
        System.out.println(compose.join());

    }

    /**
     * 演示同步执行
     */
    @Test
    public void completed11(){
        String msg = "java8";
        StringBuilder sb = new StringBuilder();
        // 阶段2
        CompletableFuture<String> stage2 = CompletableFuture.completedFuture(msg).thenApply(String::toLowerCase);
        Runnable afterRun = () ->sb.append("down");
        // 阶段1加入阶段2，两个阶段同步执行。并在这两个阶段都完成后执行afterRun
        CompletableFuture<Void> stage1 = CompletableFuture.completedFuture(msg).thenApply(String::toUpperCase).runAfterBoth(stage2, afterRun);
        stage1.join();
        System.out.println(sb.toString());
    }









}
