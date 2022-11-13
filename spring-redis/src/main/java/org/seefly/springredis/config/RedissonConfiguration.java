package org.seefly.springredis.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liujianxin
 * @date 2022/11/13 17:01
 */
@Slf4j
@Configuration
public class RedissonConfiguration {


    @Bean
    public RedissonClient redissonClient(RedisProperties properties) {
        properties.getHost();
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+properties.getHost()+":"+properties.getPort())
                .setPassword(properties.getPassword());
        log.info("[redisson] auto create redisson client using[{}]",properties.getHost());
        return Redisson.create(config);
    }
}
