package org.seefly.springredis;

import cn.worken.common.redis.EnableRedisKeyVerify;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableRedisKeyVerify
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRedisApplication.class, args);
    }

}
