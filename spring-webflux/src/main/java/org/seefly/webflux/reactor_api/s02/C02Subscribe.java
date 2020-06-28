package org.seefly.webflux.reactor_api.s02;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author liujianxin
 * @date 2020/6/23 10:28
 */
public class C02Subscribe {

    /**
     * 直接触发序列
     */
    @Test
    public void testSub1() {
        Mono.just("ok").subscribe();
    }

    /**
     * 触发后消费元素
     */
    @Test
    public void testSubAndConsumer() {
        Flux.just(1, 2, 3).subscribe(System.out::print);
    }

    /**
     * 订阅和消费，以及消费异常
     */
    @Test
    public void testSubError() {
        Flux.just(1, 2, 3).flatMap(e -> {
            if (e == 3) {
                return Mono.error(new RuntimeException("equals 3"));
            }
            return Mono.just(e);
        }).subscribe(e -> System.out.println(e), error -> {
            error.printStackTrace();
        });
    }

    /**
     * 一个消费参数
     * 一个异常处理参数
     */
    @Test
    public void testSubAndRun() {
        Flux.just(1, 2, 3).subscribe(e -> {
            System.out.println(Thread.currentThread().getName() + ":" + e);
        }, error -> error.printStackTrace(), () -> {
            System.out.println(Thread.currentThread().getName() + ":" + "消费结束");
        });
    }

    /**
     * subscribeOn不论在什么地方调用
     * 都会影响发射环境
     */
    @Test
    public void testSubscribeOn() throws InterruptedException {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

        final Flux<String> flux = Flux
                .range(1, 2)
                .map(i -> {
                    System.out.println("1"+Thread.currentThread().getName());
                    return 10 + i;
                })
                .subscribeOn(s)
                .map(i -> {
                    System.out.println("2"+Thread.currentThread().getName());
                    return "value " + i;
                });

        new Thread(() -> flux.subscribe(e -> {
            System.out.print("3"+Thread.currentThread().getName()+e);
        }),"匿名线程").start();
        Thread.sleep(3000);
    }


    /**
     * BaseSubscribe干嘛用的
     */
    @Test
    public void testBaseSubscribe(){
        BaseSubscriber<Integer> bs = new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("订阅了");
                request(1);
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println(value);
                request(1);
            }
        };

        Flux<Integer> range = Flux.range(1, 4);
        range.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> {System.out.println("Done");},
                s -> s.request(10));
        range.subscribe(bs);
    }

    @Test
    public void testSub() {
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"),
                // 最多消费2个元素
                sub -> sub.request(2));
    }
}
