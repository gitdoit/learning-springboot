package org.seefly.aop.spring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 就是一个问题:
 *  private 类型的Controller接口，可以正常访问，在方法里面也能获取到当前Controller注入的对象
 *  当被aop一下之后注入属性就null了
 *
 * 首先：
 *  通过对Spring MVC的学习可以知道，调用Controller中的接口方法最终是通过Method.invoke来的
 *  通过对Spring初始化原理的学习可以知道，代理对象的生成如果在没有循环引用的情况下是在生命周期的最后一步，即初始化后这一步骤进行替换的
 *
 * 原因：
 * 1、加载
 *  SpringMVC扫描容器加载的url和method映射，虽然扫描的是代理类生成的实例(class ProxyController$$EnhancerByCGLIB$$37bd667 extend ProxyController)，并且在这个代理类中没有privateApi方法
 *  但是通过反射能够获取父类中的这个私有方法，所以私有的privateApi()也被加载了
 * 2、调用
 *  前端请求过来，经过找到对应的Method
 *  通过Method.invoke方法调用，可以肯定的是使用的是已经被代理的类（源码可以看到是从容器中getBean）
 *  正常情况下，调用增强过后的publicApi，经过各种增强逻辑后，再在目标实例的作用域中执行这个方法，所以各种注入属性应有尽有
 *      PublicApi.inoke
 *
 *  不正常情况，调用privateApi,由于没有对这个方法做增强，所以直接就在代理类上执行了这个方法，作用域就是代理类，代理类上没有注入的各种属性
 *
 *
 *
 *
 *  PrivateApi.invoke(proxied,....)
 *      没有增强，直接执行逻辑
 *
 *
 * 更多： https://zhuanlan.zhihu.com/p/115624184
 *
 *
 * @author liujianxin
 * @date 2021/4/20 22:24
 */
@Slf4j
@Aspect
@Component
public class ControllerAspect {
    
    @Pointcut("execution(* org.seefly.aop.spring.controller.PrivateQuestionController.*(..))")
    public void pointCut() {
    }
    
    @After("pointCut()")
    public void after(JoinPoint joinPoint) {
        log.info("[ControllerAspect] [{}]执行之后",joinPoint.getSignature().getDeclaringTypeName());
    }
}
