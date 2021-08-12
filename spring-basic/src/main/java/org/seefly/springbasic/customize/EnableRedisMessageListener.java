package org.seefly.springbasic.customize;

import org.seefly.springbasic.customize.listener.RedisEventMethodProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author liujianxin
 * @date 2021/8/10 15:14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisEventMethodProcessor.class)
public @interface EnableRedisMessageListener {
}
