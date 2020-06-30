package org.seefly.webflux.reactor_api.s02;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author liujianxin
 * @date 2020/6/24 15:45
 */
public class C05Async {


    /**
     * subscribeOn不论在什么地方调用
     * 都会影响发射环境
     */
    @Test
    public void testSubscribeOn1() throws InterruptedException {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

        final Flux<String> flux = Flux
                .just(1)
                .map(i -> {
                    System.out.println("subscribeOn前:"+Thread.currentThread().getName());
                    return 10 + i;
                })
                .subscribeOn(s)
                .map(i -> {
                    System.out.println("subscribeOn后:"+Thread.currentThread().getName());
                    return "value " + i;
                });

        new Thread(() -> flux.subscribe(e -> {
            System.out.print("订阅者线程名:"+Thread.currentThread().getName());
        }),"匿名线程").start();
        Thread.sleep(3000);
    }

    @Test
    public void testSubscribeOn() throws InterruptedException {
        Scheduler scheduler = Schedulers.newElastic("thread");
        Scheduler scheduler1 = Schedulers.newElastic("thread");
        Scheduler scheduler2 = Schedulers.newElastic("thread");

        Flux<String> flux1 = Flux.just("hello ")
                .doOnNext(value -> System.out.println("Value " + value + " on :" + Thread.currentThread().getName()))
                .subscribeOn(scheduler);
        Flux<String> flux2 = Flux.just("reactive")
                .doOnNext(value -> System.out.println("Value " + value + " on :" + Thread.currentThread().getName()))
                .subscribeOn(scheduler1);
        Flux<String> flux3 = Flux.just(" world")
                .doOnNext(value -> System.out.println("Value " + value + " on :" + Thread.currentThread().getName()))
                .subscribeOn(scheduler2);
        Flux.zip(flux1, flux2, flux3)
                .map(tuple3 -> tuple3.getT1().concat(tuple3.getT2()).concat(tuple3.getT3()))
                .map(String::toUpperCase)
                .subscribe(value -> System.out.println("zip result:" + value));
        Thread.sleep(1000);
    }


    /**
     * 在publishOn之前 都是运行在调用subscribe方法的线程上
     * 在publishOn之后 则是运行在新的线程上
     * publishOn operator make the pipeline run asynchronously after being used. Which means that all previous
     * steps in the pipeline it will be executed in the main thread, and after set the operator, the rest step
     * it will be executed in the thread that you specify
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
