package org.seefly.webflux.reactor_api.s01;

import org.seefly.webflux.domain.Book;
import org.seefly.webflux.domain.InMemoryDataSource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 从每种类别的书中选出对应类别最贵的书
 *
 * @author liujianxin
 * @date 2020/5/19 17:12
 */
public class FunctionalProgramming {


    /**使用普通编程方式*/
    public static List<Book> getByCategory() {
        var books = InMemoryDataSource.books;
        Map<String, Book> map = new HashMap<>();
        for (Book book : books) {
            if (map.get(book.category()) == null) {
                map.put(book.category(), book);
            } else {
                Book book1 = map.get(book.category());
                if (book1.price().compareTo(book.price()) < 0) {
                    map.put(book.category(), book);
                }
            }
        }
        return new ArrayList<>(map.values());
    }

    /**使用函数式编程*/
    public static List<Book> getByCategoryString() {
        var books = InMemoryDataSource.books;
        return Arrays.stream(books)
                .collect(Collectors.groupingBy(Book::category))
                .values()
                .stream()
                .map(
                        bs -> bs.stream().max(Comparator.comparing(Book::price)).get()
                )
                .collect(Collectors.toList());
    }


    public static void main(String[] args) {
        System.out.println(getByCategory());
    }
}
