package org.seefly.quickstart.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liujianxin
 * @date 2018-07-27 16:23
 * 描述信息：
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BoxMessage {
}
