package org.seefly.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
        //声明序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //配置序列化
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

    //@Bean
    public RedisTemplate<String,String> lettcRedisTemplate(LettuceConnectionFactory factory){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        //将数据以UTF-8的编码方式转换为字节数据
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        stringRedisTemplate.setValueSerializer(stringRedisSerializer);
        return stringRedisTemplate;
    }



}
