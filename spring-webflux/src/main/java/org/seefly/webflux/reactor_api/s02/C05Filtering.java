package org.seefly.webflux.reactor_api.s02;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

/**
 * @author liujianxin
 * @date 2020/6/30 11:19
 */
public class C05Filtering {

    /**
     * 和Stream里面的一样
     */
    @Test
    public void testFilter(){
        Flux.just(1,2,3,4,5,6,7)
                .filter(e -> e >4)
                .subscribe(System.out::print);
    }

    /**
     * 去重
     */
    @Test
    public void testDistinct(){
        Flux.just(1,2,2,3,4,5,6,6)
                .distinct()
                .subscribe(System.out::print);
    }

    /**
     * 只拿前N个
     */
    @Test
    public void testTake(){
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .take(5)
                .subscribe(System.out::println);
    }

    /**
     * 跳过前N个
     */
    @Test
    public void testSkip(){
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .skip(5)
                .subscribe(System.out::println);
    }

    /**
     * 只要最后一个
     */
    @Test
    public void testLast(){
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .last()
                .subscribe(System.out::println);
    }
}
