package org.seefly.springbasic.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 *
 * 这一块的学习目标
 * 1、表达式
 * 2、使用方法
 * 3、原理
 *
 * 官方文档说了，切入点表达式可以用 && || ！ 也就是与或非
 * @Pointcut("execution(public * *(..))")
 * private void anyPublicOperation() {}
 *
 * @Pointcut("within(com.xyz.someapp.trading..*)")
 * private void inTrading() {}
 *
 * @Pointcut("anyPublicOperation() && inTrading()")
 * private void tradingOperation() {}
 *
 *
 * 注意事项：
 *  如果要在切面通知方法中使用JoinPoint作为参数，那么他必须要在参数的第一个位置
 * @author liujianxin
 * @date 2019-04-22 21:02
 */
@Aspect
@Component
public class MyAspect {

    /**
     * 我记得我之前写过这些学习笔记，但是代码呢？？
     * 我他妈代码丢了，还要从头再写一遍？！！
     * 这个注解我他妈还要跟自己再解释一遍吗？
     * 这个基础的东西我学过了就必须要有代码，有自己的笔记才行
     */
    @Pointcut("execution(* org.seefly.springbasic.aop.People.*(..))")
    public void pointCut(){}


    /**
     * 方法访问级别 返回值 类权限定名.方法名(参数)
     * 拦截People类中所有的方法，在其执行前执行
     *
     * 可以在这里使用JoinPoint参数，来接收目标方法的参数列表
     */
    @Before("pointCut()")
    public void beforeSing(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        if(args != null && args.length > 0){
            for(Object o : args){
                System.out.println(o);
            }
        }
        System.out.println("before:唱歌之前清清嗓子！");
    }

    /**
     * 在目标方法执行之后执行，无论是否抛出异常
     */
    @After("pointCut()")
    public void afterSing(JoinPoint joinPoint){
        System.out.println("after:唱完歌了，下台！");
    }

    /**
     * 在目标方法正常返回之后执行
     * 可以在注解中指定一个参数用来接收他的返回值
     */
    @AfterReturning(value = "pointCut()",returning = "result")
    public void returning(Object result){
        System.out.println("afterReturning:唱完歌了，返回值是"+result);
    }

    /**
     * 在目标方法抛出一场之后执行
     */
    @AfterThrowing(value = "pointCut()",throwing = "throwable")
    public void afterThrow(Throwable throwable){
        System.out.println("afterThrowing:唱歌破音了");
        throwable.printStackTrace();
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕前");
        Object proceed = joinPoint.proceed();
        System.out.println("环绕后");
        return proceed;
    }





}
