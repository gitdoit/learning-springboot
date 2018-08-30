package org.seefly.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author liujianxin
 * @date 2018-08-30 11:07
 */
@Slf4j
@Configuration
@PropertySource("classpath:privateConfig.properties")
public class RedisConfig {

    @Autowired
    private Environment env;

    @Bean("jedisConnectionFactory")
    public JedisConnectionFactory factory(){
        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
        conf.setDatabase(Integer.valueOf(env.getProperty("redis.database")));
        conf.setPort(Integer.valueOf(env.getProperty("redis.port")));
        conf.setHostName(env.getProperty("redis.host"));
        return new JedisConnectionFactory(conf);
    }

    /**
     * springboot 会给你生成两个Template
     * 一个是RedisTemplate，一个是StringRedisTemplate
     * 当然条件都是没有这个template的情况下会自动生成，用的Factor是上面配置的
     *
     * 两者异同
     * 两者的数据是不共通的；也就是说StringRedisTemplate只能管理StringRedisTemplate里面的数据，RedisTemplate只能管理RedisTemplate中的数据。
     * SDR默认采用的序列化策略有两种，一种是String的序列化策略，一种是JDK的序列化策略。
     * StringRedisTemplate默认采用的是String的序列化策略，保存的key和value都是采用此策略序列化保存的。
     * RedisTemplate默认采用的是JDK的序列化策略，保存的key和value都是采用此策略序列化保存的。
     * @param jedisConnectionFactory
     * @return
     */
    @Bean("redisTemplate")
    public RedisTemplate createRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
