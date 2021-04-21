package org.seefly.springaop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SpringAopApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SpringAopApplication.class, args);
    }
    
}
