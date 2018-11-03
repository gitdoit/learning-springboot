package org.seefly.cache.redis;

import org.junit.Test;
import org.seefly.cache.model.User;
import org.seefly.cache.redis.baseops.BaseOps;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.util.Objects;

/**
 * @author liujianxin
 * @date 2018-10-25 15:04
 */
public class Seri extends BaseOps {

    @Test
    public void testSeri() {
        objTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        objTemplate.boundValueOps("ser:jdk").set("liujianxin");
    }

    @Test
    public void testSeriJ() {
        objTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Objects.class));
        //objTemplate.boundValueOps("ser:jack").set("liujianxin");
        User u = new User();
        u.setId("1");
        u.setAge(22);
        u.setName("sdfsd");
        objTemplate.boundValueOps("ser:jackObj").set(u);
    }

    @Test
    public void testGenSeri() {
        objTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        objTemplate.boundValueOps("ser:gen").set("liujianxin");
    }

    @Test
    public void testGenSeriObj() {
        objTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        User u = new User();
        u.setId("1");
        u.setAge(22);
        u.setName("sdfsd");
        objTemplate.boundValueOps("ser:gen").set(u);
    }

}
