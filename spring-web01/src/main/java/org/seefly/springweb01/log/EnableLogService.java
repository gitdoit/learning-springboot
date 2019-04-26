package org.seefly.springweb01.log;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author liujianxin
 * @date 2019-04-25 10:29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({LogService.class})
@Documented
@EnableAspectJAutoProxy
public @interface EnableLogService {
}
