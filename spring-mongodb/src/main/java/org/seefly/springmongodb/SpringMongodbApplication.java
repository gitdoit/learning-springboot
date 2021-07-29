package org.seefly.springmongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SpringMongodbApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SpringMongodbApplication.class, args);
    }
    
}
