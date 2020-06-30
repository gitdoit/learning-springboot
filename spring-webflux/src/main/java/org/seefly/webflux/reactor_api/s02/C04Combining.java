package org.seefly.webflux.reactor_api.s02;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

/**
 * @author liujianxin
 * @date 2020/6/29 16:20
 */
public class C04Combining {

    /**
     * 压缩操作
     * 将多个Publisher组合成为一个多元组
     * 并将这个多元组发射出去
     */
    @Test
    public void zipTest() {
        Flux<String> flux1 = Flux.just("hello ");
        Flux<String> flux2 = Flux.just("reactive");
        Flux<String> flux3 = Flux.just(" world");
        Flux.zip(flux1, flux2, flux3)
                .map(tuple3 -> tuple3.getT1().concat(tuple3.getT2()).concat(tuple3.getT3()))
                .map(String::toUpperCase)
                .subscribe(value -> System.out.println("zip result:" + value));
    }

    /**
     * 合并操作
     * 用来将多个Publisher合并成为一个Flux并发射出去
     * 这个跟zip是不一样的，那个用来压缩并返回一个多元组。
     * 这个用来将多个流合并成为一个流
     *
     * 下面演示了，三个Flux，第一个延迟1秒，第二个延迟2秒，第三个不延迟,这三个都运行在不同的线程上.
     * 按道理来说，会打印hello reactive world
     * 但是每个Flux的耗时不一样，合并操作并不保证参数中的顺序和返回的顺序一致，也就是谁先返回谁先发射
     * 则会打印world reactive hello
     *
     * 若合并了多个Flux那么整个程序执行的时间就是耗时最长的那个
     */
    @Test
    public void testMerge() throws InterruptedException {
        Flux<String> flux1 = Flux.just("hello").publishOn(Schedulers.elastic()).doOnNext(value -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Flux<String> flux2 = Flux.just("reactive").publishOn(Schedulers.elastic()).doOnNext(value -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Flux<String> flux3 = Flux.just("world").publishOn(Schedulers.elastic());
        Flux.merge(flux1, flux2, flux3)
                .map(String::toUpperCase)
                .subscribe(System.out::println);
        Thread.sleep(3000);
    }

    /**
     * 顺序执行，前一个执行完毕后一个才执行，所以保证返回顺序
     * 坏处是整个程序的耗时是累加的
     */
    @Test
    public void testConcat() throws InterruptedException {
        Flux<String> flux1 = Flux.just("hello").publishOn(Schedulers.elastic()).doOnNext(value -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Flux<String> flux2 = Flux.just("reactive").publishOn(Schedulers.elastic()).doOnNext(value -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Flux<String> flux3 = Flux.just("world").publishOn(Schedulers.elastic());
        Flux.concat(flux1, flux2, flux3)
                .map(String::toUpperCase)
                .subscribe(System.out::println);
        Thread.sleep(3000);
    }

    /**
     * 如果没有发射任何数据的话，那么启用备用Publisher来发射
     */
    @Test
    public void testsSwitchIfEmpty() {
        Flux.empty()
                .switchIfEmpty(Flux.just("Switch flux"))
                .subscribe(System.out::println);
    }

    /**
     * 和switchIfEmpty类似，但是参数不一样
     * 一个接收Publisher一个接收唯一数据
     */
    @Test
    public void testDefaultIfEmpty(){
        Flux.empty()
                .defaultIfEmpty("Fuck")
                .subscribe(System.out::print);
    }

    /**
     * https://stackoverflow.com/questions/28175702/what-is-the-difference-between-flatmap-and-switchmap-in-rxjava
     * 在单线程的情况下和flatMap没有什么区别
     * 但是在多线程的情况下，会区别很大。
     * 情景：实时搜索
     * 用户在搜索框中实时的打字，前端将这个过程中的输入实时的传给后端
     * 例如5个过程量传给后端，同时搜索，如果其中某个搜索完毕有结果了，那么就取消其他4个
     *
     */
    @Test
    public void testSwitchMap() throws InterruptedException {
        Flux.just(112, 22, 64, 88, 5)
                .switchMap(value -> Flux.just(value)
                        .publishOn(Schedulers.elastic())
                        .doOnNext(this::silence))
                .subscribe(e -> System.out.println(e));
                // .subscribe(System.out::println);
        Thread.sleep(1000);
    }

    public void silence(int mis){
        try {
            Thread.sleep(mis);
        } catch (InterruptedException e) {
            System.out.println("取消了:"+mis);
        }
    }

}
