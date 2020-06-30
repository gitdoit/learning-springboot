package org.seefly.webflux.reactor_api.s03;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.seefly.webflux.domain.Book;
import org.seefly.webflux.domain.BookQuery;
import org.seefly.webflux.domain.InMemoryDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 使用路由定义接口
 * @author liujianxin
 * @date 2020/6/30 15:33
 */

@Configuration
public class C02RouterBased {
    private static final String PATH_PREFIX = "/routed/";

    private final Validator validator;
    private final ObjectMapper objectMapper;

    public C02RouterBased(Validator validator, ObjectMapper objectMapper) {
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    @Bean
    public RouterFunction<ServerResponse> routers(){
        return RouterFunctions.route()
                .POST(PATH_PREFIX+"book",this::findByPage)
                .build();
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        String isbn = request.pathVariable("isbn");
        return InMemoryDataSource.findBookMonoById(isbn)
                .flatMap(book -> ServerResponse.ok().bodyValue(book))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * 分页查询
     */
    private Mono<ServerResponse> findByPage(ServerRequest request){
        // 绑定&校验参数
        return ReactiveControllerHelper.queryParamsToMono(request,objectMapper, BookQuery.class,validator)
                // 查询
                .flatMap(bookQuery -> ServerResponse.ok().bodyValue(InMemoryDataSource.findBooksByQuery(bookQuery)));
    }

    /**
     * 删除
     */
    private Mono<ServerResponse> delete(ServerRequest request){
        String isbn = request.pathVariable("isbn");
        return InMemoryDataSource.findBookMonoById(isbn)
                .flatMap(e -> {
                    InMemoryDataSource.removeBook(e);
                    return ServerResponse.ok().build();
                }).switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * 更新
     */
    private Mono<ServerResponse> update(ServerRequest request){
        String isbn = request.pathVariable("isbn");
        return InMemoryDataSource.findBookMonoById(isbn)
                .flatMap(book ->
                    ReactiveControllerHelper.queryParamsToMono(request,objectMapper, Book.class,validator)
                            .doOnNext(p -> {
                                InMemoryDataSource.saveBook(p);
                            })
                            .flatMap(ob -> ServerResponse.ok().location(UriComponentsBuilder.fromPath("").build().toUri()).build())
                            .switchIfEmpty(ServerResponse.notFound().build())
                );
    }

}
