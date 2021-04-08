package org.seefly.springbasic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBasicApplication {
    
    public static void main(String[] args) {
        //ConfigurableApplicationContext run = SpringApplication.run(SpringBasicApplication.class, args);
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SpringBasicApplication.class);
        builder.listeners((ApplicationListener< ApplicationReadyEvent>) event -> {
            System.out.println("通过手动注册的事件监听器收到了：ApplicationReadyEvent事件");
        });
        builder.build().run(args);
    }
}
