package org.seefly.springaop.config;

import org.seefly.springaop.component.People;
import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import org.springframework.aop.framework.adapter.DefaultAdvisorAdapterRegistry;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 *
 * 【注册BeanPostProcessor】
 * 1、传入主配置类，创建IOC容器
 * 2、创建完成后调用{@link AbstractApplicationContext#refresh()}刷新容器
 * 3、调用 {@link AbstractApplicationContext#registerBeanPostProcessors} 就是创建bean的后置处理器，来拦截bean的创建。
 * 4、先在bean工厂中拿出所有BeanPostProcessor的实现类，调用一下beanFactory.getBean方法，
 *    1）、调用{@link AbstractBeanFactory#getBean}
 *          1>{@link AbstractBeanFactory#doGetBean}
 *              2> {@link DefaultSingletonBeanRegistry#getSingleton(java.lang.String, org.springframework.beans.factory.ObjectFactory)}
 *                  3>{@link AbstractAutowireCapableBeanFactory#createBean(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, java.lang.Object[])}
 *
 *                     这个方法注释说给后置处理器一个机会，如果后置处理器返回了一个代理Bean，则就用后置处理器代理的那个，否则就创建。并直接返回
 *                     4> {@link AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition)}
 *
 *                          这块会调用所有的注册的{@link InstantiationAwareBeanPostProcessor}的实例的postProcessBeforeInstantiation方法
 *                          AOP代理注册的那个也属于他的实现类，所以在这里会被应用。
 *                         5> {@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInstantiation(java.lang.Class, java.lang.String)}
 *
 *                      实际创建Bean的流程
 *                      ->{@link AbstractAutowireCapableBeanFactory#doCreateBean}
 *                          1、{@link AbstractAutowireCapableBeanFactory#createBeanInstance} 真正的创建bean
 *                          2、{@link AbstractAutowireCapableBeanFactory#populateBean} 填充属性
 *                              这里有一点值得注意，就是InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation方法
 *                              会在这里起作用，每个类创建流程到这之后，根据这个方法的返回值判断一下它应不应该被填充属性。默认true也就是填充
 *                          3、{@link AbstractAutowireCapableBeanFactory#initializeBean} 初始化bean
 *                              1)、{@link AbstractAutowireCapableBeanFactory#invokeAwareMethods}引用所有的Aware接口的回调方法
 *                              2)、{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization}调用一下所有的bean的后置处理器
 *                              3)、{@link AbstractAutowireCapableBeanFactory#invokeInitMethods} 调用一下bean的初始化方法，例如InitializingBean接口
 *                              4)、{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization}调用后置处理器的after
 *
 *  5、上面已经将后置处理器创建完毕，然后需要对他们进行排序
 *      这些其中实现了PriorityOrdered的先注册，其次是实现了Ordered的，再其次就是啥都没实现的。
 *      ps:这里面就有@EnableAspectJAutoProxy里面注册的哪个internalAutoProxyCreator的定义信息
 *
 *  6、将后置处理器放到 {@link AbstractBeanFactory#addBeanPostProcessor}
 *      这一步会判断你注册进去的后置处理器有没有实现InstantiationAwareBeanPostProcessor接口，有的话，那就将工厂里面的
 *      hasInstantiationAwareBeanPostProcessors属性设置为true，意味着resolveBeforeInstantiation()这方法就起作用了，啥作用呢
 *      就是每个Bean实际创建之前给这些特殊的后置处理器一个机会，让他们去创建。
 *
 *
 *  ======================={@link AspectJAwareAdvisorAutoProxyCreator}的作用(如何创建代理对象)=================================================================
 *  如何创建？
 *      乍一看以为postProcessBeforeInitialization方法是创建代理对象的核心，其实不是。想一想也是，虽然它用来代理创建Bean，但是
 *      如果在这一步创建代理对象的话，那么后面的属性填充、Aware接口调用都执行不到了。所以是在postProcessAfterInitialization这块去创建代理对象的
 *      这个时候也初始化完了啥都填充了。我觉得逻辑应该是这样的，先找到所有的切面类缓存一下信息，然后在初始化完成调用后置处理器的时候根据这些
 *      信息去代理需要代理的Bean方法。
 *  1、
 *
 *  1、postProcessBeforeInstantiation逻辑
 *      它把切面类放到一个缓存里面(advisedBeans)
 *
 *
 *  2、postProcessAfterInitialization创建代理对象
 *    1)、{@link AbstractAutoProxyCreator#wrapIfNecessary(java.lang.Object, java.lang.String, java.lang.Object)}
 *      这里判断每个bean是否需要被代理
 *      判断是否在advisedBeans里
 *      在执行postProcessBeforeInstantiation里的判断逻辑
 *      上面都没有的话就执行到这里
 *      -> {@link AbstractAdvisorAutoProxyCreator#getAdvicesAndAdvisorsForBean(java.lang.Class, java.lang.String, org.springframework.aop.TargetSource)}
 *          ->{@link AbstractAdvisorAutoProxyCreator#findEligibleAdvisors(java.lang.Class, java.lang.String)}
 *              这里是找到候选的增强器
 *          -> {@link AbstractAdvisorAutoProxyCreator#findAdvisorsThatCanApply(java.util.List, java.lang.Class, java.lang.String)}
 *              这一步是根据候选增强器以及当前的Bean来找到能应用在当前Bean上的增强器
 *
 *      -> 如果当前这个Bean需要被代理的话，就会有代理增强器返回，然后就会将当前Bean放到advisedBeans里面，表示后面不需要再处理了。
 *         然后调用{@link AbstractAutoProxyCreator#createProxy(java.lang.Class, java.lang.String, java.lang.Object[], org.springframework.aop.TargetSource)}
 *         来真正的创建代理对象，上面的找到可用的增强器方法返回的东西像是一个“拦截器链”，也就是方法执行链，用来当作创建代理对象的参数。
 *         这样在调用增强过后的方法时，就是根据执行链来执行了
 *
 *              ->{@link DefaultAopProxyFactory#createAopProxy(org.springframework.aop.framework.AdvisedSupport)}
 *                  根据被代理类型来创建不同的代理对象
 *                  1、ObjenesisCglibAopProxy
 *                  2、JdkDynamicAopProxy
 *       ->将代理对象返回，这时候就是一个被增强过的代理对象了
 *
 *
 *=====================================调用增强后方法的过程===============================================================================
 *
 *
 * 上面已经将对象增强了，这时候被增强的对象(已经成了一个CglibAopProxy)中已经含有被织入的切面方法了。
 * 如果此时从容器中获取这个对象，那么它是被增强过的了。下面看一下被增强过的对象，调用它被增强的方法的调用过程
 *
 *
 * 1、由于调用的是被增强后的方法，所以会执行到 CglibAopProxy.DynamicAdvisedInterceptor#intercept
 *
 * 1、CglibAopProxy.DynamicAdvisedInterceptor#intercept
 *      // 创建连接器链
 *      -> List chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
 *             ->  // 这个在上面创建代理对象的时候，已经放入了
 *                 Advisor[] advisors = config.getAdvisors();
 *                 // 根据这个长度创建拦截器链列表
 * 		            List<Object> interceptorList = new ArrayList<>(advisors.length);
 * 		            然后遍历这些增强器,然后将之当作参数调用下面这个方法
 * 		            MethodInterceptor[] interceptors = {@link DefaultAdvisorAdapterRegistry#getInterceptors(org.springframework.aop.Advisor)}
 * 		                -> 判断是不是MethodInterceptor类型，是的话就直接放到返回数组里面
 * 		                -> 不是的话，就判断这个增强器是不是Before\AfterReturning\Throwing这种类型的增强器，是的话适配一下，放到返回值数组里。
 *
 *      ->  // 如果没有的话就直接调用目标方法
 *
 *      -> Object retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
 *          // 如果有的话就创建一个下面这东西，其实每个通知方法都包装成一个方法拦截器，组装成一个方法拦截器链。
 *          // 执行目标方法的过程就是出发拦截器链的过程
 *  2、CglibMethodInvocation的proceed()方法调用过程。
 *      1、CglibMethodInvocation的父类ReflectiveMethodInvocation实现了MethodInvocation接口的proceed方法，
 *          所以它也是一个MethodInvocation实例，这就有意思了！！
 *          上面创建的拦截器连，每个拦截器都是一个MethodInterceptor实例，这个接口只有一个抽象方法(Object invoke(MethodInvocation invocation) throws Throwable)
 *          看到没有，这个抽象方法的参数就是MethodInvocation。
 *      2、在CglibMethodInvocation.proceed()方法内，有一个计数器，每调用一次proceed方法就自增一次
 *          调用第一个拦截器的.invoke(this)方法，把自己传进去。然后这个拦截器执行完自己的逻辑之后再调用CglibMethodInvocation.proceed
 *          就这样环环相扣，遍历完所有的拦截器。这个套路是不是很熟悉啊，spring的web拦截器也是这个操作，spring security也是这个操作。这个东西
 *          看起来跟递归好像，但是递归只能递归自己，这个能‘递归’其他东西，只要是实现了指定的接口就行。
 *      3、示例之 AspectJAfterThrowingAdvice
 *          随便拿一个拦截器来看看它的代码，很简单SB都能理解
 *
 *          public Object invoke(MethodInvocation mi) throws Throwable {
 * 		        try {
 * 		                // 直接调用下一个拦截器
 * 			            return mi.proceed();
 *                }
 * 		        catch (Throwable ex) {
 * 			        if (shouldInvokeOnThrowing(ex)) {
 * 			            // 抛异常我就调用通知方法
 * 				        invokeAdviceMethod(getJoinPointMatch(), null, ex);
 *                 }
 *                 // 然后抛出去
 * 			        throw ex;
 *             }
 *           }
 *
 *
 *   拦截器链调用总结
 *   无非就是 MethodInvocation.proceed() 和 MethodInterceptor.invoke(MethodInvocation invocation) 他俩之间的链式调用
 *   MethodInvocation的实例负责组装拦截器链以及维护调用顺序
 *   MethodInterceptor的实例负责具体的逻辑也就是在适当的时候调用通知方法，和调用MethodInvocation.proceed()以推进拦截器链的前进
 *
 *   还有我们常用的通知方法对应的拦截器
 *   1、ExposeInvocationInterceptor就是在ThreadLocal中放些参数，不知道干啥的
 *   2、AspectJAfterThrowingAdvice异常通知，总是在前面，这样才能拦截到后面的所有异常，跟spring security的异常翻译器是不是一个道理?
 *   3、AfterReturningAdviceInterceptor 返回通知
 *   4、AspectJAfterAdvice 返回后通知
 *   5、AspectJAroundAdvice 环绕通知
 *
 *
 *
 *
 *
 *
 *
 *
 * @author liujianxin
 * @date 2019-05-04 20:16
 */
public class AopInitFlow {
    
    @org.junit.Test
    public void test() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AopConfig.class);
        System.out.println("容器初始化完毕...");
        People bean = applicationContext.getBean(People.class);
        bean.sing("sdf");
        System.out.println("sdfsdf");
        bean.sing("sdf");
    }
    
}
