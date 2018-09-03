package org.seefly.cache.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 自定义aop缓存
 * 实现二级缓存：ehcache+redis
 * @author liujianxin
 * @date 2018-09-03 17:58
 */
@Slf4j
@Component
@Aspect
public class CacheAop {

    private ExpressionEvaluator<String> evaluator = new ExpressionEvaluator<>();
    @Autowired
    private EhCacheCacheManager manager;


    /**
     * TODO 添加二级缓存redis
     */
    @Around("@annotation(org.seefly.cache.cache.Cachebel) && @annotation(cachebel)")
    public Object cache(ProceedingJoinPoint pjp, Cachebel cachebel) throws Throwable {
        String cacheName = cachebel.cacheName();
        Assert.isTrue(!StringUtils.isEmpty(cacheName),"缓存不能为空！");
        Cache cache = manager.getCache(cacheName);
        Assert.notNull(cache,"没有这样的缓存！");
        String key = cachebel.key();
        Assert.isTrue(!StringUtils.isEmpty(key),"主键不能为空！");
        key = getValue(pjp,key);
        Class returnType=((MethodSignature)pjp.getSignature()).getReturnType();
        Object value = cache.get(key, returnType);
        if(value != null){
            return value;
        }
        Object proceed = pjp.proceed();
        cache.put(key,proceed);
        return proceed;
    }


    private String getValue(JoinPoint joinPoint, String condition) {
        return getValue(joinPoint.getTarget(), joinPoint.getArgs(),
                joinPoint.getTarget().getClass(),
                ((MethodSignature) joinPoint.getSignature()).getMethod(), condition);
    }

    private String getValue(Object object, Object[] args, Class clazz, Method method,
                            String condition) {
        if (args == null) {
            return null;
        }
        EvaluationContext evaluationContext =
                evaluator.createEvaluationContext(object, clazz, method, args);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
        return evaluator.condition(condition, methodKey, evaluationContext, String.class);
    }


}
