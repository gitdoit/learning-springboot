package org.seefly.webflux.reactor_api.s02;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Mono创建响应式流
 *
 * @author liujianxin
 * @date 2020/6/18 16:27
 */
public class C01MonoApi {

    /**
     * 使用三种异步方式创建响应式流
     */
    @Test
    public void monoAsyncTest() {
        // 使用Mono.fromCallable创建
        // fromCallable:订阅
        // fromCallable:elastic-2@2020-06-18T16:25:38.741498600
        Mono<String> stringMono = Mono.
                fromCallable(() -> Thread.currentThread().getName() + "@" + LocalDateTime.now())
                .publishOn(Schedulers.elastic());
        blockMono("fromCallable", stringMono);

        // fromRunnable:订阅
        // elastic-3@2020-06-18T16:25:38.754463800
        Mono<Object> fromRunnable = Mono.
                fromRunnable(() -> System.out.println(Thread.currentThread().getName() + "@" + LocalDateTime.now()))
                .publishOn(Schedulers.elastic());
        blockMono("fromRunnable", fromRunnable);
    }

    /**
     * 使用just创建
     */
    @Test
    public void monoJustTest(){
        Mono<Integer> just = Mono.just(1);
        blockMono("monoJust",just);
    }

    /**
     * 使用thenReturn
     */
    @Test
    public void mapTest(){
        Mono<String> stringMono = Mono.just("world")
                .map(e -> "hello " + e)
                // 这里打印 hello world
                .doOnNext(System.out::println)
                // 最终返回一个值，之后的消费者接收到的都是下面这个值了
                .thenReturn("do something else")
                // 打印do something else
                .doOnNext(e -> System.out.println("after thenReturn:"+e));
        blockMono("map",stringMono);
    }

    @Test
    public void mono2FluxTest(){
        Flux<Integer> flux = Mono.just(1).flux();
        Mono<List<Integer>> listMono = Flux.just(1).collectList();
    }

    /**
     * 将多个mono合并成一个
     */
    @Test
    public void zipTest(){
        var userId = "max";
        var monoProfile = Mono.just(userId + "的详细信息");
        var monoLatestOrder = Mono.just(userId + "的最新订单");
        var monoLatestReview = Mono.just(userId + "的最新评论");
        var zipMono = Mono.zip(monoProfile, monoLatestOrder, monoLatestReview)
                .doOnNext(t -> System.out.printf("%s的主页，%s, %s, %s%n", userId, t.getT1(), t.getT2(), t.getT3()));
        blockMono("zipMono", zipMono);
    }




    public void blockMono(String name, Mono<?> mono) {
        mono.doOnSubscribe(s -> System.out.println(name + ":订阅"))
                .doOnNext(e -> System.out.println(name + ":" + e))
                .block();
    }
}
