package org.seefly.springweb01.spring;

import org.seefly.springweb01.log.EnableLogService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableLogService
public class SpringWeb01Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringWeb01Application.class, args);
    }
}
