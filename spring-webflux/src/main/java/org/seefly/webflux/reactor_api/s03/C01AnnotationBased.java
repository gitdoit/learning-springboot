package org.seefly.webflux.reactor_api.s03;

import org.seefly.webflux.domain.Book;
import org.seefly.webflux.domain.InMemoryDataSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Optional;

/**
 *
 * 结合spring web注解的方式使用响应式编程
 * @author liujianxin
 * @date 2020/6/30 13:49
 */
@RequestMapping("/anno")
@RestController
public class C01AnnotationBased {

    /**
     * 列表
     */
    @GetMapping("/books")
    public Flux<Book> list(){
        return Flux.fromIterable(InMemoryDataSource.findAllBooks());
    }

    /**
     * 创建
     */
    @PostMapping("/book")
    public Mono<ResponseEntity<?>> create(@Valid @RequestBody Book book, UriComponentsBuilder ucb){
        InMemoryDataSource.saveBook(book);
        return Mono.just(
                ResponseEntity.created(
                        ucb.path("/anno/book/").path(book.getIsbn()).build().toUri()
                ).build()
        );
    }

    /**
     * 查询
     */
    @GetMapping("/book/{isbn}")
    public Mono<ResponseEntity<Book>> find(@PathVariable String isbn){
//        Optional<Book> bookById = InMemoryDataSource.findBookById(isbn);
//        if(!bookById.isPresent()){
//            System.out.println("没有找到:"+isbn);
//            return Mono.just(ResponseEntity.notFound().build());
//        }
//        return Mono.just(ResponseEntity.ok(bookById.get()));
        // 链式编程
        return Mono.just(InMemoryDataSource.findBookById(isbn))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }



}
