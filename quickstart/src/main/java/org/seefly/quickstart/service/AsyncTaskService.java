package org.seefly.quickstart.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author liujianxin
 * @date 2018-08-01 10:48
 * 描述信息：测试异步执行注解
 **/
@Service
public class AsyncTaskService {

    /**
     * 通过@Async注解表明该方法是个异步方法，如果注解在类级别，则表明该类所有的方法都是异步方法。
     * 而这里的方法自动被注入使用ThreadPoolTaskExecutor作为TaskExecutor
     */
    @Async
    public void executeAsyncTask(Integer i){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("executeAsyncTask:"+i);

    }

    @Async
    public void executeAsyncTaskPlus(Integer i){
        System.out.println("executeAsyncTaskPlus:"+i);
        throw  new RuntimeException("sdfs");
    }
}
