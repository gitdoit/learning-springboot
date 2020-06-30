package org.seefly.webflux.reactor_api.s02;

import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

/**
 * 创建响应式流
 * Flux
 *  just
 *  fromArray
 *  fromStream
 *  range
 *  generate
 *  interval
 *  defer
 *
 *
 * Mono
 *  just
 *  empty
 *  fromRunnable
 *  fromCallable
 *
 *
 * @author liujianxin
 * @date 2020/6/18 15:52
 */
public class C01Creating {


    /**
     * 创建Flux
     */
    @Test
    public void testFluxCreate(){
        Flux<Integer> just = Flux.just(1, 2, 3, 4, 5, 6);
        Flux<Integer> integerFlux = Flux.fromArray(new Integer[]{1, 2, 3, 4});
        Flux<Integer> fromStream = Flux.fromStream(Stream.of(1, 2, 3, 4));
        Flux<Integer> range = Flux.range(1, 10);

        Flux<Object> generate = Flux.generate(() -> 1, (state, sink) -> {
            sink.next("message #" + state);
            if (state == 10) {
                sink.complete();
            }
            return state + 1;
        });
    }

    /**
     * 创建Mono
     */
    @Test
    public void testMonoCreate(){
        Mono<Object> empty = Mono.empty();
        // just创建
        Mono<String> just = Mono.just("a");
        // 异步线程创建
        Mono<String> stringMono = Mono.
                fromCallable(() -> Thread.currentThread().getName() + "@" + LocalDateTime.now())
                .publishOn(Schedulers.elastic());
        // 创建一个空的Mono
        Mono<Object> fromRunnable = Mono.
                fromRunnable(() -> System.out.println(Thread.currentThread().getName() + "@" + LocalDateTime.now()))
                .publishOn(Schedulers.elastic());
    }

    /**
     * 使用生成器来创建序列
     */
    @Test
    public void testGen(){
        Flux.generate(() -> 0,(state,sink) -> {
            sink.next("3 x " + state + " = " + 3*state);
            if (state == 10) sink.complete();
            return state + 1;
        }).subscribe(e-> System.out.println(e));
    }

    /**
     * 循环间隔
     * 从0开始按照指定时间间隔发射自增的值
     * 需要手动取消。和js里面的类似
     */
    @Test
    public void testInterval() throws InterruptedException {
        Disposable subscribe = Flux.interval(Duration.of(1, ChronoUnit.SECONDS))
                .map(value -> {
                    System.out.println(Thread.currentThread().getName());
                    return value * 10;
                })
                .subscribe(value -> System.out.println("间隔值:" + value));
        System.out.println("是否启动："+subscribe.isDisposed());
        Thread.sleep(10000);
        subscribe.dispose();
        System.out.println("调用dispose:"+subscribe.isDisposed());
    }

    /**
     * 延迟，在订阅的时候才计算发射的值
     */
    private String value = "Hello";
    @Test
    public void defer(){
        Flux<String> flux = Flux.defer(() -> Flux.just(value));
        value = "hello reactive world";
        flux.subscribe(value -> System.out.println("更新后的value值："+value));
    }




    /**
     * 看看再创建元素的时候阻塞一下是什么效果
     */
    @Test
    public void testStop() throws InterruptedException {
        Disposable subscribe = Flux.just(1, 2, 3).publishOn(Schedulers.elastic()).map(e -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            return e;
        }).subscribe(e -> System.out.println(e));
        System.out.println("主线程");
        Thread.sleep(1000);
        subscribe.dispose();
    }




    private void subscribeFlux(String name, Flux<?> flux) {
        flux.doOnSubscribe(s -> System.out.println(name + "订阅触发"))
                .doOnNext(s -> System.out.println("onNext:" + s))
                .doOnComplete(() -> System.out.println(name + "结束！"))
                .subscribe();
    }


}
