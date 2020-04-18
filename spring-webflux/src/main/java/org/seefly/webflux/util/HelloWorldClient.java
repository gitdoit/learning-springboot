package org.seefly.webflux.util;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author liujianxin
 * @date 2020/4/18 21:21
 */
public class HelloWorldClient {

    public String getResult(){
        WebClient client = WebClient.create("http://localhost:8080");
        Mono<ClientResponse> exchange = client.get().uri("/hello").accept(MediaType.TEXT_PLAIN).exchange();
        return exchange.flatMap(res -> res.bodyToMono(String.class)).block();
    }
}
