package org.seefly.webflux;

import org.seefly.webflux.util.HelloWorldClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 */
@SpringBootApplication
public class SpringWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebfluxApplication.class, args);
        HelloWorldClient client = new HelloWorldClient();
        System.out.println("访问结果："+client.getResult());
    }

}
