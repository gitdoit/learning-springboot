package org.seefly.springweb01.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditCut {
    /***操作平台**/
    String platform();
    /***操作功能名称***/
    String funcName() default "";
}
