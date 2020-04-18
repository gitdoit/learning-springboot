package org.seefly.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author liujianxin
 * @date 2020/4/18 20:49
 */
public class BaseApiTest {

    @Test
    public void testFirst(){

        Mono.just("hello")
                .map(e -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    System.out.println("publish之前:"+Thread.currentThread());
                    return e;
                })
                .publishOn(Schedulers.newSingle("thread-a"))
                .map(e ->{
                    System.out.println("publish之后:"+Thread.currentThread());
                    return e;
                })
                .subscribe();
        System.out.println("主线程："+Thread.currentThread());
    }
}
