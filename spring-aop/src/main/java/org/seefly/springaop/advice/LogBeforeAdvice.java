package org.seefly.springaop.advice;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author liujianxin
 * @date 2019/8/20 20:27
 */
public class LogBeforeAdvice implements MethodBeforeAdvice {
    
    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println("准备执行方法" + method.getName() + " 参数列表" + Arrays.toString(objects));
    }
}
