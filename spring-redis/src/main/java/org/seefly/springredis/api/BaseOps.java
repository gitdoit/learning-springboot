package org.seefly.springredis.api;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author liujianxin
 * @date 2018-08-30 14:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public abstract class BaseOps {
    @Autowired
    protected StringRedisTemplate stringTemplate;
    @Resource(name = "redisTemplate")
    protected RedisTemplate objTemplate;

}
