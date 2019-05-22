package org.seefly.transaction.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
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
