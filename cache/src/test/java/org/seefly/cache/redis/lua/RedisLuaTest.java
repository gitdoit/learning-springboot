package org.seefly.cache.redis.lua;

import org.junit.Test;
import org.seefly.cache.redis.baseops.BaseOps;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author liujianxin
 * @date 2018-10-26 13:24
 */
public class RedisLuaTest extends BaseOps {

    @Resource(name = "lettcRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testLua(){
        stringRedisTemplate.multi();
        stringRedisTemplate.boundValueOps("letc").set("sdfsdf");
        stringRedisTemplate.exec();
    }


}
