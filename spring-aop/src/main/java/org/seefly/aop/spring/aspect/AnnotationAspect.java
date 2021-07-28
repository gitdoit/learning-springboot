package org.seefly.aop.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * 演示拦截标记指定注解的方法的执行
 *
 * 现实需求
 *   程序使用微服务的方式进行拆分，所以一个模块会部署多个实例，但是其中某些方法
 *   需要进行同步控制，即同一个时间内多个实例下的相同的方法只能有一个执行
 * 方案
 *   利用Spring AOP配合Redis实现的分布式锁来实现
 *   1、自定义注解
 *   2、在需要同步的方法上标记这个注解
 *   3、利用SpringAOP的 around 来对这些方法进行切片
 *   4、执行前获取分布式锁
 *      成功-去执行
 *      失败-抛出异常
 *
 *
 * @author liujianxin
 * @date 2021/7/23 11:07
 **/
@Slf4j
@Aspect
@Component
public class AnnotationAspect implements ApplicationContextAware {
    private ApplicationContext applicationContext;


    @Around("@annotation(distributedLock)")
    public Object around( ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        Method targetMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        DistributedLock annotation = AnnotationUtils.findAnnotation(targetMethod, DistributedLock.class);
        Assert.notNull(annotation,"can not found DistributedLock Annotation on target Method!");
        log.info("[AspectWithAnnotation] around-before execute,annotation value is:{}",annotation.key());
        String key = this.getKey(joinPoint);
        System.out.println("==================="+key);

        Object proceed = joinPoint.proceed();
        log.info("[AspectWithAnnotation] around-after execute.");
        return proceed;
    }

    @Override
    public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface DistributedLock{
        String key() default "";
    }



    private String getKey( ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Object target = joinPoint.getTarget();
        Object[] arguments = joinPoint.getArgs();
        DistributedLock annotation = AnnotationUtils.findAnnotation(targetMethod, DistributedLock.class);
        String spel=null;
        if(annotation != null){
            spel = annotation.key();
        }
        return this.parse(target,spel, targetMethod, arguments);
    }

    /**
     * https://stackoverflow.com/questions/11616316/programmatically-evaluate-a-bean-expression-with-spring-expression-language
     */
    private  String parse(Object rootObject,String spel, Method method, Object[] args) {
        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        ExpressionParser parser = new SpelExpressionParser();

        StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject,method,args,u);

        context.setBeanResolver(new BeanFactoryResolver(applicationContext));

        context.addPropertyAccessor(new BeanExpressionContextAccessor());


        Expression expression = parser.parseExpression(spel, new TemplateParserContext());

        String value = expression.getValue(context, String.class);

        return value;


    }
}
