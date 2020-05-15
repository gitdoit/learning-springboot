package org.seefly.spring.jetcache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "org.seefly.spring.jetcache")
@SpringBootApplication
public class SpringJetcacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJetcacheApplication.class, args);
    }

}
