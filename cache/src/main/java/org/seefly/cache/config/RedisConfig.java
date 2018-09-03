package org.seefly.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.OxmSerializer;
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

    /**
     * 连接池
     * @return
     */
    @Bean("jedisConnectionFactory")
    public JedisConnectionFactory factory() {
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
     * <p/>
     * 两者异同
     * 两者的数据是不共通的；也就是说StringRedisTemplate只能管理StringRedisTemplate里面的数据，RedisTemplate只能管理RedisTemplate中的数据。<br>
     * SDR默认采用的序列化策略有两种，一种是String的序列化策略，一种是JDK的序列化策略。<br>
     * StringRedisTemplate默认采用的是String的序列化策略，保存的key和value都是采用此策略序列化保存的。<br>
     * RedisTemplate默认采用的是JDK的序列化策略，保存的key和value都是采用此策略序列化保存的。<br>
     * <p/>
     * 序列化方式<br>
     * {@link StringRedisSerializer} 用utf-8编/解码，只能处理类型为String的序列化<br>
     * {@link JdkSerializationRedisSerializer} 用字节流序列化，处理的对象需要实现序列化接口<br>
     * {@link Jackson2JsonRedisSerializer} 用json形式序列化，封装度较高<br>
     * {@link OxmSerializer} 用xml形式序列化<br>
     *
     * @param jedisConnectionFactory 配置工厂
     * @return 模板
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> createRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        //键序列化策略，若使用系统提供的字符串序列化方式，如果key为非string类型，会报错
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //值序列化策略，用json形式序列化数据
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }
}
