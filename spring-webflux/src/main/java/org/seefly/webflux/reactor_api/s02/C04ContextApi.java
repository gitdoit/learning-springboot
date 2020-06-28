package org.seefly.webflux.reactor_api.s02;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author liujianxin
 * @date 2020/6/22 11:15
 */
public class C04ContextApi {


    @Test
    public void testContext(){
        String key = "message";
        Mono<String> r = Mono.just("Hello")
                .flatMap( s ->
                        Mono.subscriberContext().map( ctx -> s + " " + ctx.get(key))
                )
                .subscriberContext(ctx -> ctx.put(key, "World"));

        StepVerifier.create(r)
                .expectNext("Hello World")
                .verifyComplete();
    }
}
