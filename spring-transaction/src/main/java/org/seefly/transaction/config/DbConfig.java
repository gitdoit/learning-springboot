package org.seefly.transaction.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * 1、引入
 *  mybatis-plus-boot-starter
 *      spring-boot-jdbc-start
 *          spring-jdbc
 *              spring-tx
 * 2、自动配置
 *   TransactionAutoConfiguration
 *      DataSourceTransactionManagerAutoConfiguration
 *          DataSourceTransactionManager
 *          咱用的是DataSource，它就自动配置一个这种事务管理
 *      HibernateJpaAutoConfiguration
 *          Hibernate的事务管理器，咱不用，条件不满足，不会产生。
 *      TransactionTemplate
 *          还额外往容器里仍了一个编程式事务控制，咱没用过，看起来自由度挺高的，应该试试
 *          不知道能不能用这个解决多数据源事务控制的问题。
 *
 *  3、声明式事务工作原理
 *      首先声明式事务是依据咱在业务方法上添加的@Transactional注解来控制的，那么肯定就
 *      跟spring的aop一样了，扫描注解读取信息，增强目标方法，创建代理对象放到容器里。
 *
 *      1）、EnableTransactionManagement注解在事务自动配置类里被使用
 *          然后这个注解引入了TransactionManagementConfigurationSelector，然后这个selector根据注解中的条件往容器里
 *          仍组件，啥条件啊，就是注解中的增强器类型属性，默认是PROXY。
 *          这时候就会放入AutoProxyRegistrar、ProxyTransactionManagementConfiguration这俩类
 *      2）、AutoProxyRegistrar
 *          自动代理注册？啥玩意啊，看看源码，就是又引入了InfrastructureAdvisorAutoProxyCreator，是一个后置处理器
 *          给目标对象创建代理对象，然后代理对象里面包含拦截器链。跟AOP一样的流程
 *
 *      3）、ProxyTransactionManagementConfiguration
 *          作用就是往容器里面仍一个事务增强器，这个增强器仍容器里面之后上面步骤2的自动代理注册就可以拿到了，然后在
 *          后置处理的过程中用这个增强器增强对应的bean。
 *      4）、TransactionInterceptor
 *          这个在步骤3创建增强器的时候作为一个属性放进去的，它就是真正实现事务控制逻辑的东西。而平台事务管理器就在它这里
 *          被使用。具体逻辑可以看{@link TransactionAspectSupport#invokeWithinTransaction(java.lang.reflect.Method, java.lang.Class, org.springframework.transaction.interceptor.TransactionAspectSupport.InvocationCallback)}
 *          （事务拦截器实现方法拦截器继承拦截器继承切面，被增强器引用。）
 *
 *  核心类结构介绍
 *  https://segmentfault.com/a/1190000018281577
 *
 *
 *
 *
 * PlatformTransactionManager 平台事务管理器，spring主要的基础设置
 * 只定义了3个核心方法
 * 1、getTransaction(@Nullable TransactionDefinition definition)
 *     根据事务的传播行为创建或者返回一个已激活的事务，若是返回一个
 *     已经激活的事务，那么隔离级别和超时时间将不起作用。这个里面说的
 *     几个参数应该就是@Transactional注解上的参数。
 * 2、commit(TransactionStatus status) throws TransactionException;
 *      如果不是一个新事物提交，它的提交将不起作用。意思就是传播行为的原因
 *      把之前的那个事务启动，然后一起提交。
 * 3、rollback(TransactionStatus status) throws TransactionException;
 *      如果不是一个新事物，则设置 callback-only
 *
 * AbstractPlatformTransactionManager  抽象的事务管理器，spring 继承它来实现自己的事务管理器
 *  它提供了下面的流程处理
 *  1、确认是否存在一个事务
 *  2、应用事务传播级别
 *  3、必要的时候暂停或者启动事务
 *  4、提交的时候检查 rollback-only 标志位
 *  5、回滚的时候适当的修改，就是设置rollback-only标志位吧
 *  6、触发已注册的同步回调(这是啥)
 *
 * DataSourceTransactionManager myBatis用的事务管理器
 *
 *
 * @author liujianxin
 * @date 2019-05-21 14:57
 */
@Configuration
@MapperScan("org.seefly.transaction.mapper")
public class DbConfig {

}
