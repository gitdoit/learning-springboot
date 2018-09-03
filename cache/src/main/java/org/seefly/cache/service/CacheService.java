package org.seefly.cache.service;

import org.seefly.cache.cache.Cachebel;
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

    @Cachebel(key="#id",expireTime = 3600)
    public String getInfo(String id){
        System.out.println("get from redis");
        return template.boundValueOps(id).get();
    }


    public String getBySel(){
        return null;
    }
}
