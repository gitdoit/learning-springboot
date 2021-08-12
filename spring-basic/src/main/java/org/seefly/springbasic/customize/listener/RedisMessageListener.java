package org.seefly.springbasic.customize.listener;

import java.lang.annotation.*;

/**
 * 分布式锁的aop实现方式
 *
 * @author liujianxin
 * @date 2021/7/23 17:17
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisMessageListener {

    String topic();

    int consumerCount() default 1;

}
