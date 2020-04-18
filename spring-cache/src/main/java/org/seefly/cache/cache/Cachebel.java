package org.seefly.cache.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liujianxin
 * @date 2018-09-03 17:57
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cachebel {
    /*缓存主键*/
    String key();
    /*缓存名称*/
    String cacheName() default "longTimeCache";
    /*存活时间*/
    int expireTime() default 3600;
}
