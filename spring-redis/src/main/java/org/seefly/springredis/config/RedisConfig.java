package org.seefly.springredis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.*;

/**
 *
 * 官方文档<a href="https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#get-started"></a>
 *
 * @author liujianxin
 * @date 2018-08-30 11:07
 */
@Slf4j
@Configuration
public class RedisConfig {


    /**
     * <p/>
     * 两者异同
     * 两者的数据是不共通的；也就是说StringRedisTemplate只能管理StringRedisTemplate里面的数据，RedisTemplate只能管理RedisTemplate中的数据。<br>
     * SDR默认采用的序列化策略有两种，一种是String的序列化策略，一种是JDK的序列化策略。<br>
     * StringRedisTemplate默认采用的是String的序列化策略，保存的key和value都是采用此策略序列化保存的。<br>
     * RedisTemplate默认采用的是JDK的序列化策略，保存的key和value都是采用此策略序列化保存的。<br>
     * <p/>
     * 序列化方式<br>
     * StringRedisSerializer            用utf-8编/解码，只能处理类型为String的序列化<br>
     * JdkSerializationRedisSerializer  用字节流序列化，处理的对象需要实现序列化接口<br>
     * Jackson2JsonRedisSerializer      用json形式序列化，封装度较高<br>
     * OxmSerializer                    用xml形式序列化<br>
     *
     * @param lettuceConnectionFactory 配置工厂
     * @return 模板
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> createRedisTemplate(RedisConnectionFactory lettuceConnectionFactory) {
        //声明序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer(){
            @Override
            public byte[] serialize(String string) {
                return super.serialize("SOS:"+string);
            }
        };
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        //配置序列化
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        //键序列化策略，若使用系统提供的字符串序列化方式，如果key为非string类型，会报错
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //值序列化策略，用json形式序列化数据
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate lettcRedisTemplate(RedisConnectionFactory jedisConnectionFactory){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setEnableTransactionSupport(true);
        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        //将数据以UTF-8的编码方式转换为字节数据
        return stringRedisTemplate;
    }


    /**
     * 为了使spring支持redis注解形式的事务声明，这里需要添加一个事务管理器
     * @param dataSource
     * @return
     */
    //@Bean
    /*public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }*/

}
