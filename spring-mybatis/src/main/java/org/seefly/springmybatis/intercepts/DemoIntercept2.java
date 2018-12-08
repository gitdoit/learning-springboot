package org.seefly.springmybatis.intercepts;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Statement;
import java.util.Properties;

/**
 * 演示多插件开发
 * 多插件开发根据插件配置的顺序层层包装目标方法，第一个包装的插件会在最里层，第二个在它外层
 * 所以在执行的时候肯定从外层到里层去执行，也就是说后包装的插件先执行。
 * @author liujianxin
 * @date 2018-12-08 16:15
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "parameterize", args = Statement.class)})
public class DemoIntercept2 implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("DemoIntercept2----"+invocation.getMethod());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        System.out.println("DemoIntercept2--包装"+target.getClass());
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
