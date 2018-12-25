package org.seefly.springweb01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringWeb01Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringWeb01Application.class, args);
    }
}
