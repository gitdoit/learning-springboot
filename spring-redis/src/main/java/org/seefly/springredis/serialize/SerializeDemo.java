package org.seefly.springredis.serialize;

import org.junit.Test;
import org.seefly.springredis.api.BaseOps;
import org.seefly.springredis.model.User;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 演示使用不同序列化方式序列化对象的异同
 * @author liujianxin
 * @date 2018-10-25 15:04
 */
public class SerializeDemo extends BaseOps {

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
        objTemplate.setKeySerializer(new StringRedisSerializer());
        objTemplate.opsForValue().set("","");
        objTemplate.boundValueOps("ser:stringSer").set("StringRedisSerializer");
    }


    /**
     * Jackson2JsonRedisSerializer序列化字符串
     */
    @Test
    public void testSeriJ() {
        objTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        objTemplate.boundValueOps("ser:JacksonString").set("Jackson2JsonRedisSerializer");
    }

    /**
     * Jackson2JsonRedisSerializer序列化对象
     */
    @Test
    public void testSerJacksonString(){
        objTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
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
        System.out.println("sdfsdfsdf");
        System.out.println(o);
    }

}
