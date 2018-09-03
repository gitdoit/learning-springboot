package org.seefly.cache.controller;

import org.seefly.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @RequestMapping("/get")
    public String test(String id) {

        return service.getInfo(id);
    }
}
