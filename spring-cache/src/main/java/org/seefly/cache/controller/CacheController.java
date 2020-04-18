package org.seefly.cache.controller;

import org.seefly.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujianxin
 * @date 2018-09-03 16:24
 */
@RestController
public class CacheController {

    @Autowired
    private CacheService service;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @RequestMapping("/get")
    public String test(String id) {

        return service.getInfo(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/trans")
    public String trans(){
        stringRedisTemplate.boundValueOps("trans:str1").set("aaaa");
        //int i = 1 / 0;
        stringRedisTemplate.boundValueOps("trans:str2").set("bbbb");
        return "OK";
    }
}
