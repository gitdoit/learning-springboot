package org.seefly.webflux.reactor_api.s02;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 创建响应式流
 *
 * @author liujianxin
 * @date 2020/6/18 15:52
 */
public class C01FluxApi {

    /**
     * 演示如何创建一个响应式流
     * just fromArray fromStream
     */
    @Test
    public void create() {
        // Stream.of() 就这个意思
        // Flux<Integer> just = Flux.just(1, 2, 3, 4, 5, 6);
        // subscribeFlux("Flux.just",just);

        // 从数组创建一个流
        // Flux<Integer> integerFlux = Flux.fromArray(new Integer[]{1, 2, 3, 4});
        // subscribeFlux("integerFlux",integerFlux);

        // Flux<Integer> fromStream = Flux.fromStream(Stream.of(1, 2, 3, 4));
        // subscribeFlux("fromStream",fromStream);

        Flux<Integer> range = Flux.range(1, 10);
        subscribeFlux("range", range);
    }


    /**
     * 使用generate函数来生成
     */
    @Test
    public void createFluxProgrammatically() {
        Flux<Object> generate = Flux.generate(() -> 1, (state, sink) -> {
            sink.next("message #" + state);
            if (state == 10) {
                sink.complete();
            }
            return state + 1;
        });
        subscribeFlux("generate", generate);
    }

    @Test
    public void mapTest() {
        Flux<String> map = Flux.just(1, 2, 3).map(e -> "map:" + e);
        subscribeFlux("map", map);
    }

    /**
     * 拉平展开
     */
    @Test
    public void flatMapTest() {
        Flux<Integer> integerFlux = Flux.just(1, 2, 3).flatMap(e -> Mono.just(e));
        subscribeFlux("flatMap", integerFlux);
    }

    /**
     * doOnError
     * 如果发生了异常并且没有用doOnError方法处理
     * 那么会直接抛出打印异常栈
     * 使用doOnError处理的话，就像是try{}了一样
     * 但是没有返回值，同时终结这个流
     */
    @Test
    public void doOnErrorTest() {
        var throwExceptionFlux = Flux.range(1, 10).map(i -> {
            if (i > 5) {
                throw (new RuntimeException("Something wrong"));
            }
            return "item #" + i;
        }).doOnError(e -> System.out.println("发生了异常"));
        subscribeFlux("throwExceptionFlux", throwExceptionFlux);
    }


    /**
     * onErrorReturn
     * 当异常发生的时候，将他捕获，然后返回一个其他值，并且通道会关闭。
     * 这时后面的观察者就观察不到这个异常了
     * <p>
     * doOnError
     * 有异常发生的时候的做法
     */
    @Test
    public void onErrorReturnTest() {
        var errorFlux = Flux.range(1, 10).flatMap(i -> {
            if (i > 5) {
                return Mono.error(new RuntimeException("Something wrong"));
            }
            return Mono.just("item #" + i);
        }).onErrorReturn("fuck! exception !!");
        subscribeFlux("throwExceptionFlux", errorFlux);
    }

    /**
     * 这个和onErrorReturn不同
     * 前者只会在发生异常的时候返回一个值
     * 而这个会创建一个新的流来替换出异常的
     */
    @Test
    public void onErrorResumeTest() {
        var errorFlux = Flux.range(1, 10).flatMap(i -> {
                        if (i > 5) {
                            return Mono.error(new RuntimeException("Something wrong"));
                        }
                        return Mono.just("item #" + i);
                }).
                // 异常发生的时候使用其他流恢复
                onErrorResume(e -> Flux.range(6, 10).map(i -> "i???#" + i));
        subscribeFlux("onErrorResumeTest", errorFlux);
    }


    private void subscribeFlux(String name, Flux<?> flux) {
        flux.doOnSubscribe(s -> System.out.println(name + "订阅触发"))
                .doOnNext(s -> System.out.println("onNext:" + s))
                .doOnComplete(() -> System.out.println(name + "结束！"))
                .subscribe();
    }


}
