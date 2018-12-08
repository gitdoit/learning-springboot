package org.seefly.springmybatis.intercepts;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Statement;
import java.util.Properties;

/**
 * {@link Intercepts}注解告诉myBatis这个插件需要拦截哪些四大对象的哪些方法,所以它需要一个签名数组作为参数。
 * {@link Signature}签名注解，里面需要三个参数，第一个是你要拦截哪个四大对象，第二个你要拦截哪个方法，第三个是这个方法有哪些参数(避免重载)
 * 这里演示拦截StatementHandle的参数设置
 *
 * @author liujianxin
 * @date 2018-12-08 15:13
 */

@Intercepts({@Signature(type = StatementHandler.class, method = "parameterize", args = Statement.class)})
public class DemoIntercepts implements Interceptor {


    /**
     * 处理逻辑
     *
     * @param invocation 目标方法
     * @return 处理结果
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler s = (RoutingStatementHandler)invocation.getTarget();
        s.getBoundSql().getParameterObject();
        System.out.println("DemoIntercepts拦截的方法"+invocation.getMethod().getName());
        return invocation.proceed();
    }

    /**
     * 包装对象
     * 通过源码可以直到，在四大对象创建之后都会调用一下这个方法{@link InterceptorChain#pluginAll}
     * 这个方法会循环遍历调用每个注册过的插件的下面这个方法
     * 而下面这个方法简单的使用了myBatis提供的包装逻辑
     * 它里面的包装步骤就是取插件上注解中的签名信息，然后再跟四大对象一一比对，如果相符
     * 则进行包装，不相符就不包装。所以这个包装逻辑可以自己重写
     *
     *
     * @param target 四大对象
     * @return 包装后的四大对象
     */
    @Override
    public Object plugin(Object target) {
        // MyBatis提供的简易包装
        // 点击去看一啊，拦截的四大对象的那些方法，必须是四大对象接口规定的，而四大对象的实现它们自己新加的肯定不行，
        // 肯定会报找不到目标方法，我瞎几把猜的
        System.out.println("DemoIntercepts--->包装---" + target.getClass());
        return Plugin.wrap(target, this);
    }

    /**
     * 设置参数
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {

    }
}
