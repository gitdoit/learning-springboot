package org.seefly.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author liujianxin
 * @date 2018-09-03 16:32
 */
@Service
public class CacheService {
    @Autowired
    private StringRedisTemplate template;

    @Cacheable(key="#id",value = "testCache")
    public String getInfo(String id){
        return template.boundValueOps(id).get();
    }
}
