package org.seefly.concurrent;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

/**
 * 超大型工具类
 * @see <a href="https://github.com/manouti/completablefuture-examples">来源</a>
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

    private static String delayedToUpper(String str){
        randomSleep();
        return str.toUpperCase();
    }
    private static String delayedToLower(String str){
        randomSleep();
        return str.toLowerCase();
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
        CompletableFuture<String> cf = CompletableFuture.completedFuture("java8").thenApplyAsync(CompletableFutureDemo::delayedToLower);
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
            return delayedToUpper(p);
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
        CompletableFuture<String> fc = CompletableFuture.completedFuture("java8").thenApplyAsync(CompletableFutureDemo::delayedToUpper);
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
        CompletableFuture<String> fc = CompletableFuture.completedFuture(msg).thenApplyAsync(CompletableFutureDemo::delayedToUpper);
        CompletableFuture<String> fc1 = CompletableFuture.completedFuture(msg).thenApplyAsync(CompletableFutureDemo::delayedToLower);
        // 将fc 和 fc1 组合，谁先完成用谁的结果进行计算
        CompletableFuture<String> compose = fc.applyToEither(fc1, s -> s + " from applyToEither");
        System.out.println(compose.join());

    }

    /**
     * 演示同步执行
     * 阶段1同步加入阶段2，两个阶段同步执行。并在这两个阶段都完成后执行afterRun。
     */
    @Test
    public void completed11(){
        String msg = "java8";
        StringBuilder sb = new StringBuilder();
        CompletableFuture<String> stage2 = CompletableFuture.completedFuture(msg).thenApply(String::toLowerCase);
        Runnable afterRun = () ->sb.append("down");
        CompletableFuture<Void> stage1 = CompletableFuture.completedFuture(msg).thenApply(String::toUpperCase).runAfterBoth(stage2, afterRun);
        stage1.join();
        System.out.println(sb.toString());
    }

    /**
     * 阶段1同步加入阶段2，并在两个阶段计算完成后使用两个计算结果进行操作。
     */
    @Test
    public void completed12(){
        String msg = "java8";
        CompletableFuture<String> stage2 = CompletableFuture.completedFuture(msg).thenApply(String::toLowerCase);
        CompletableFuture<String> stage1 = CompletableFuture.completedFuture(msg).thenApply(CompletableFutureDemo::delayedToUpper).thenCombine(stage2, (s1, s2) -> s1 + s2);
        // 同步操作，会立即得到结果
        System.out.println(stage1.getNow(null));
    }

    /**
     * 阶段1异步加入阶段2，在两个阶段都完成后使用两个阶段产生的结果进行操作
     */
    @Test
    public void completed13(){
        String msg = "java8";
        CompletableFuture<String> stage2 = CompletableFuture.completedFuture(msg).thenApply(String::toLowerCase);
        CompletableFuture<String> fc = CompletableFuture.completedFuture(msg).thenApplyAsync(CompletableFutureDemo::delayedToUpper).thenCombine(stage2, (s1, s2) -> s1 + s2);
        //异步操作，需要等待结果
        System.out.println(fc.join());
    }

    /**
     * 使用thenCompose，将调用者所产生的计算结果作为下一个阶段计算时所需要的参数。
     * 这是不是像一条流水线？
     */
    @Test
    public void completed14(){
        String msg = "java8";
        CompletableFuture<String> state2 = CompletableFuture.completedFuture(msg).thenApply(CompletableFutureDemo::delayedToLower);
        CompletableFuture<String> fc = CompletableFuture.completedFuture(msg).thenApply(CompletableFutureDemo::delayedToUpper).thenCompose(upper -> state2.thenApply(s -> upper + s));
        System.out.println(fc.join());
    }

    /**
     * anyOf:
     *  返回一个新的CompletableFuture，它在任何给定的CompletableFutures完成时完成，结果相同。
     *  否则，如果异常完成，则返回的CompletableFuture也会这样做，并且CompletionException将此异常作为其原因。
     *  如果未提供CompletableFutures，则返回不完整的CompletableFuture
     */
    @Test
    public void completed15() {
        StringBuilder sb = new StringBuilder();
        List<String> msgs = Arrays.asList("a","b","c");
        CompletableFuture[] futures = msgs.stream().map(msg -> CompletableFuture.completedFuture(msg).thenApply(CompletableFutureDemo::delayedToUpper)).toArray(CompletableFuture[]::new);
        // 由于是同步的，所以这里会立即得到一个CompletableFuture，并在whenComplete中进行计算
        CompletableFuture.anyOf(futures).whenComplete((res, exception)->{
            if(exception == null){
                assertTrue(StringUtils.isAllUpperCase((String)res));
                sb.append(res);
            }
        });
        System.out.println(sb.toString());
    }

    /**
     * allOf
     *  返回在所有给定的CompletableFutures完成时完成的新CompletableFuture。
     *  如果任何给定的CompletableFutures异常完成，则返回的CompletableFuture也会这样做，并且CompletionException将此异常作为其原因。,
     *  否则，给定的CompletableFutures的结果（如果有的话）不会反映在返回的CompletableFuture中，但可以通过单独检查它们来获得。,
     *  如果未提供CompletableFutures，则返回CompletableFuture，其值为null。,
     *  此方法的应用之一是等待在继续程序之前完成一组独立的CompletableFutures，如：CompletableFuture.allOf（c1，c2，c3）.join（）;
     */
    @Test
    public void completed16(){
        StringBuilder sb = new StringBuilder();
        List<String> msgs = Arrays.asList("a","b","c");

        CompletableFuture[] futures = msgs.stream().map(msg -> CompletableFuture.completedFuture(msg).thenApply(CompletableFutureDemo::delayedToUpper)).toArray(CompletableFuture[]::new);
        // 在所有的future完成时，返回一个新的future。由于是同步的，所以执行到这里肯定全都计算完毕了
        CompletableFuture.allOf(futures).whenComplete((res,exception) ->{
            for(CompletableFuture f : futures){
                assertTrue(StringUtils.isAllUpperCase((String)f.getNow(null)));
                sb.append("done");
            }
        });
        System.out.println(sb.toString());
    }

    /**
     * 异步的等待所有计算完成！
     */
    @Test
    public void completed17(){
        StringBuilder sb = new StringBuilder();
        List<String> msgs = Arrays.asList("a","b","c");
        // 注意这里换成了异步计算
        CompletableFuture[] futures = msgs.stream().map(msg -> CompletableFuture.completedFuture(msg).thenApplyAsync(CompletableFutureDemo::delayedToUpper)).toArray(CompletableFuture[]::new);
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures).whenComplete((res, exception) -> {
            for (CompletableFuture f : futures) {
                assertTrue(StringUtils.isAllUpperCase((String) f.getNow(null)));
                sb.append("done");
            }
        });
        //异步执行，需要等待它完成
        allOf.join();
        System.out.println(sb.toString());
    }










}
