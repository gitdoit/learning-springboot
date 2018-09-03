package org.seefly.cache.redisops;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liujianxin
 * @date 2018-08-30 14:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class BaseOps {
    @Autowired
    protected StringRedisTemplate stringTemplate;
    @Autowired
    protected RedisTemplate<String, Object> objTemplate;

}
