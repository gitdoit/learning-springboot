package org.seefly.aop.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * TODO 这一块的学习目标 1、表达式 2、使用方法 3、原理
 *
 * @author liujianxin
 * @date 2019-04-22 21:02
 */
@Slf4j
@Aspect
@Component
public class BasicAspect {
    
    /**
     * 我记得我之前写过这些学习笔记，但是代码呢？？ 我他妈代码丢了，还要从头再写一遍？！！ 这个注解我他妈还要跟自己再解释一遍吗？ 这个基础的东西我学过了就必须要有代码，有自己的笔记才行
     * <p>
     * 官方文档说了，切入点表达式可以用 && || ！ 也就是与或非
     * <pre> <code>
     * @ Pointcut("execution(public * *(..))")
     * private void anyPublicOperation() {
     * }
     * @ Pointcut("within(com.xyz.someapp.trading..*)")
     * private void inTrading() {
     * }
     * @ Pointcut("anyPublicOperation() && inTrading()")
     * private void tradingOperation(){
     * }
     * <code/>  <pre/>
     *
     *
     * execution(方法访问级别 返回值 类权限定名.方法名(参数) )
     */
    @Pointcut("execution(* org.seefly.aop.spring.service.UserService.*(..))")
    public void pointCut() {
    }
    
    
    /**
     *
     * 可以在这里使用JoinPoint参数，来接收目标方法的参数列表
     * 注意事项： 如果要在切面通知方法中使用JoinPoint作为参数，那么他必须要在参数的第一个位置
     */
    @Before("pointCut()")
    public void beforeSing(JoinPoint joinPoint) {
        if (joinPoint.getTarget() instanceof Comparable) {
            log.info(joinPoint.getTarget().toString());
        }
        
        log.info("[BasicAspect-before] 执行[{}]方法,参数[{}]",joinPoint.getSignature().getDeclaringTypeName(),Arrays.asList(joinPoint.getArgs()));
    }
    
    /**
     * 在目标方法执行之后执行，无论是否抛出异常
     */
    @After("pointCut()")
    public void afterSing(JoinPoint joinPoint) {
        log.info("[BasicAspect-after] 执行[{}]方法之后",joinPoint.getSignature().getDeclaringTypeName());
    }
    
    /**
     * 在目标方法正常返回之后执行 可以在注解中指定一个参数用来接收他的返回值
     */
    @AfterReturning(value = "pointCut()", returning = "result")
    public void returning(JoinPoint joinPoint,Object result) {
        log.info("[BasicAspect-returning] 执行[{}]方法返回,结果为[{}]",joinPoint.getSignature().getDeclaringTypeName(),result);
    }
    
    /**
     * 在目标方法抛出一场之后执行
     */
    @AfterThrowing(value = "pointCut()", throwing = "throwable")
    public void afterThrow(JoinPoint joinPoint,Throwable throwable) {
        log.error("[BasicAspect-throwing] 执行"+joinPoint.getSignature().getDeclaringTypeName()+"方法异常",throwable);
    }
    
    /**
     *  换绕执行
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[BasicAspect-around-before] 执行[{}]方法",joinPoint.getSignature().getDeclaringTypeName());
        Object proceed = joinPoint.proceed();
        log.info("[BasicAspect-around-after] 执行[{}]方法",joinPoint.getSignature().getDeclaringTypeName());
        return proceed;
    }
    
    
}
