package org.seefly.webflux.reactor_api.s02;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Mono创建响应式流
 * https://stackoverflow.com/questions/49115135/map-vs-flatmap-in-reactor
 *
 * @author liujianxin
 * @date 2020/6/18 16:27
 */
public class C03Transforming {


    /**
     * 使用thenReturn
     */
    @Test
    public void mapTest() {
        Mono<String> stringMono = Mono.just("world")
                .map(e -> "hello " + e)
                // 这里打印 hello world
                .doOnNext(System.out::println)
                // 最终返回一个值，之后的消费者接收到的都是下面这个值了
                .thenReturn("do something else")
                // 打印do something else
                .doOnNext(e -> System.out.println("after thenReturn:" + e));
        blockMono("map", stringMono);
    }


    /**
     * 拉平展开
     * 和map相比，它需要返回一个Publisher，这意味着
     * 返回值不是一个简单的转换关系，所以可以使用各种Mono或者Flux的API
     * 完成更丰富的操作。
     */
    @Test
    public void flatMapTest() {
        Flux.just("hello")
                .flatMap(value -> Flux.just("flatMap")
                        .map(newValue -> value.concat(" ").concat(newValue))
                        .flatMap(lastValue -> Flux.just("operator")
                                .map(newValue -> lastValue.concat(" ").concat(newValue))))
                .map(String::toUpperCase)
                .subscribe(System.out::println);
    }

    /**
     * 1 1 1 1 1
     * 2 2 2 2 2
     * 3 3 3 3 3
     */
    @Test
    public void testFlatmap() throws InterruptedException {
        Flux.range(1, 5)
                .flatMap(e ->
                        Flux.range(1, 3)
                                .publishOn(Schedulers.elastic())
                                .map(s -> {
                                    try {
                                        // System.out.println(Thread.currentThread().getName());
                                        Thread.sleep(500);
                                    } catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    }
                                    return s;
                                })
                )
                .subscribe(System.out::println);
        Thread.sleep(20000);
    }

    /**
     * scan方法给定两个值，一个是初始化，另一个是计算方式
     * 这一点和stream中的reduce很像。
     * 运算逻辑：
     * result1 = initValue
     * result2 = 计算方式(result1,source1)
     * result3 = 计算方式(result2,source1)
     * 。。。。
     */
    @Test
    public void testScan() {
        Flux.just(1,2,3,4,5,6,7,8,9,10)
                .scan(new ArrayList<Integer>(),(list,newValue) ->{
                    list.add(newValue);
                    return list;
                })
                .subscribe(System.out::print);
    }

    /**
     * This is good!
     * 将这个流中的元素都收集到一起,然后进行组装
     * 和这个类似: Stream.of(1,2,3).collect(Collectors.toSet())
     */
    @Test
    public void compose() throws InterruptedException {

        Flux.just(1, 2, 3, 4, 5)
                .flatMap(e -> {
                    try {
                        // 会等它睡完的
                        Thread.sleep(500);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    return Mono.just(e);
                })
                .compose(Flux::collectList)
                .doOnNext(value -> System.out.println("Thread:" + Thread.currentThread().getName()))
                .subscribe(System.out::println);
        // Thread.sleep(1000);
    }

    /**
     * window窗口操作
     * 等到了指定数量元素够的时候，就把他们分成一组成为了一个Flux
     */
    @Test
    public void testWindow(){
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                // 五个一组，合成一个Flux
                .window(5)
                .flatMap(flux -> {
                    // 10 / 5 = 2 ,这里打印两次
                    System.out.println("New Flux:" + flux);
                    return flux;
                })
                .subscribe(System.out::println);
    }


    @Test
    public void testThen() {
        Flux.just(1, 2).map(e -> {
            System.out.println(Thread.currentThread().getName());
            return e;
        }).then().map(e -> {
            System.out.println(e);
            return 3;
        }).subscribe();
    }


    @Test
    public void mono2FluxTest() {
        Flux<Integer> flux = Mono.just(1).flux();
        Mono<List<Integer>> listMono = Flux.just(1).collectList();
    }




    public void blockMono(String name, Mono<?> mono) {
        mono.doOnSubscribe(s -> System.out.println(name + ":订阅"))
                .doOnNext(e -> System.out.println(name + ":" + e))
                .block();
    }
}
