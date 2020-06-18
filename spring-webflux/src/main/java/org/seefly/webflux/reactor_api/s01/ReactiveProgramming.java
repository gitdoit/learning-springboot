package org.seefly.webflux.reactor_api.s01;

import org.seefly.webflux.domain.Book;
import org.seefly.webflux.domain.InMemoryDataSource;
import reactor.core.publisher.Flux;

import java.util.Comparator;

/**
 * @author liujianxin
 * @date 2020/5/19 17:11
 */
public class ReactiveProgramming {


    /**响应式编程*/
    public static void main(String[] args) {
        Flux.just(InMemoryDataSource.books)
                .collectMultimap(Book::category)
                .flatMapMany(m -> Flux.fromIterable(m.entrySet()))
                .flatMap(e -> Flux.fromIterable(e.getValue()).sort(Comparator.comparing(Book::price).reversed()).next())
                .doOnNext(System.out::print)
                .subscribe();
    }
}
