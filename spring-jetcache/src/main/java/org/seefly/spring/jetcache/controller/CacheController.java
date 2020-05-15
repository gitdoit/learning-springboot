package org.seefly.spring.jetcache.controller;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * https://github.com/alibaba/jetcache
 * @author liujianxin
 * @date 2020/5/14 15:57
 */
@RestController
@RequestMapping
public class CacheController {
    private final AtomicInteger adder = new AtomicInteger();

    @Cached(expire = 8, cacheType = CacheType.LOCAL)
    @GetMapping("/cache/one")
    public Map<String,String> cacheOne(){
        Map<String,String> map = new HashMap<>();
        map.put("count",adder.incrementAndGet()+"");
        return map;
    }
}
