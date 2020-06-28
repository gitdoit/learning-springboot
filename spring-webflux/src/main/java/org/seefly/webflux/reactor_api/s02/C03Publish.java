package org.seefly.webflux.reactor_api.s02;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author liujianxin
 * @date 2020/6/24 15:45
 */
public class C03Publish {

    /**
     * 在publishOn之前 都是运行在调用subscribe方法的线程上
     * 在publishOn之后 则是运行在新的线程上
     */
    @Test
    public void testPublicOn() throws InterruptedException {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

        final Flux<String> flux = Flux
                .range(1, 2)
                .map(i -> {
                    System.out.println("1"+Thread.currentThread().getName());
                    return 10 + i;
                })
                .publishOn(s)
                .map(i -> {
                    System.out.println("2"+Thread.currentThread().getName());
                    return "value " + i;
                });

        new Thread(() -> flux.subscribe(e -> {
            System.out.print("3"+Thread.currentThread().getName()+e);
        }),"匿名线程").start();
        Thread.sleep(3000);
    }
}
