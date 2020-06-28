package org.seefly.webflux.reactor_api.s02;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Mono创建响应式流
 * https://stackoverflow.com/questions/49115135/map-vs-flatmap-in-reactor
 * @author liujianxin
 * @date 2020/6/18 16:27
 */
public class C02Maps {


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


    /**
     * 拉平展开
     */
    @Test
    public void flatMapTest() {
        Flux<Integer> integerFlux = Flux.just(1, 2, 3).flatMap(e -> Mono.just(e));
    }

    @Test
    public void testThen(){
        Flux.just(1,2).map(e -> {
            System.out.println(Thread.currentThread().getName());
            return e;
        }).then().map(e -> {
            System.out.println(e);
            return 3;
        }).subscribe();
    }

    @Test
    public void testFlatmap() throws InterruptedException {
        System.out.println("sdfsf");
        Flux.range(1,5).flatMap(e -> {
            return Flux.range(1,3).publishOn(Schedulers.elastic()).map(s -> {
                try {
                    // System.out.println(Thread.currentThread().getName());
                    Thread.sleep(500);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                return s;
            });
        }).subscribe(e -> {
            System.out.println(e);
        });
        Thread.sleep(20000);
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
