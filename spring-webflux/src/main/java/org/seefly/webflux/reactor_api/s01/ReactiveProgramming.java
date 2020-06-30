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


    public static Flux<Book> getMostExpensiveBooksByCategoryReactive(Flux<Book> books) {
        return books.collectMultimap(Book::getCategory)
                .flatMapMany(m -> Flux.fromIterable(m.entrySet()))
                .flatMap(e -> Flux.fromIterable(e.getValue())
                        .sort(Comparator.comparing(Book::getPrice).reversed())
                        .next());
    }

    public static void main(String[] args) {
        var pipeline = getMostExpensiveBooksByCategoryReactive(Flux.just(InMemoryDataSource.books));
        pipeline = pipeline.doOnNext(System.out::println);
        System.out.println("什么都不会发生，直到pipeline开始");
        pipeline.subscribe();
    }
}
