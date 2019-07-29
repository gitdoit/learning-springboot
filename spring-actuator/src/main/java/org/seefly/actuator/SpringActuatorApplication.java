package org.seefly.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringActuatorApplication.class, args);
    }

}
