package org.seefly.springaop.advice;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * @author liujianxin
 * @date 2019/8/20 20:29
 */
public class LogAfterAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
        System.out.println("方法"+ method.getName()+"执行完毕，返回值"+o);
    }
}
