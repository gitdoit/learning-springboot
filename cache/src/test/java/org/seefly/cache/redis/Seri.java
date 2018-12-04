package org.seefly.cache.redis;

import org.junit.Test;
import org.seefly.cache.model.User;
import org.seefly.cache.redis.baseops.BaseOps;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Objects;

/**
 * @author liujianxin
 * @date 2018-10-25 15:04
 */
public class Seri extends BaseOps {

    /**
     * JDK序列化方式序列化字符串
     */
    @Test
    public void testSeri() {
        objTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        objTemplate.boundValueOps("ser:jdkString").set("JdkSerializationRedisSerializer");
    }

    /**
     * String序列化方式序列化字符串
     */
    @Test
    public void testStringSer(){
        objTemplate.setValueSerializer(new StringRedisSerializer());
        objTemplate.boundValueOps("ser:stringSer").set("StringRedisSerializer");
    }


    /**
     * Jackson2JsonRedisSerializer序列化字符串
     */
    @Test
    public void testSeriJ() {
        objTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Objects.class));
        objTemplate.boundValueOps("ser:JacksonString").set("Jackson2JsonRedisSerializer");
    }

    /**
     * Jackson2JsonRedisSerializer序列化对象
     */
    @Test
    public void testSerJacksonString(){
        objTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Objects.class));
        User u = new User("1","redis",9);
        objTemplate.boundValueOps("ser:JacksonObject").set(u);
    }

    /**
     * GenericJackson2JsonRedisSerializer序列化字符串
     */
    @Test
    public void testGenSeri() {
        objTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        objTemplate.boundValueOps("ser:GenericString").set("GenericJackson2JsonRedisSerializer");
    }

    /**
     * GenericJackson2JsonRedisSerializer序列化对象
     */
    @Test
    public void testGenSeriObj() {
        objTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        User u = new User("1","redis",9);
        objTemplate.boundValueOps("ser:GenericObject").set(u);
    }

    /**
     * 反序列化
     */
    @Test
    public void testGetObject(){
        objTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        BoundValueOperations<String, Object> ops = objTemplate.boundValueOps("ser:GenericObject");
        User o = (User)ops.get();
        System.out.println(o);
    }

}
