package org.seefly.juc.future.jdk;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 使用jdk自带的
 * @author liujianxin
 * @date 2018-11-22 14:15
 */
public class FutrueMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new JdkRealData("a"));
        Executors.newFixedThreadPool(1).submit(futureTask);
        System.out.println("请求完毕！");
        Thread.sleep(2000);
        System.out.println("数据："+futureTask.get());
    }
}
