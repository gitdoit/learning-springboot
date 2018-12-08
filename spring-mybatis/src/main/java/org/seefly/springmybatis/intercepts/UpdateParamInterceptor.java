package org.seefly.springmybatis.intercepts;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.sql.Statement;
import java.util.Properties;

/**
 * 这个插件用来演示拦截{@link StatementHandler#parameterize}的设置参数方法，拦截之后将参数替换
 *
 * {@link SystemMetaObject}这个东西MyBatis提供的工具，能够拿到四大对象中的属性，还能修改。
 * 但是前提要知道四大对象中有哪些属性。
 *
 * 例如：四大对象之一的StatementHandle的抽象实现类{@link BaseStatementHandler}主要的俩参数如下
 *      1、{@link ResultSetHandler} 处理结果集，属性名：resultSetHandler
 *          MyBatis只给了一个默认实现{@link DefaultResultSetHandler}
 *              属性：
 *                  rowBounds  分页信息
 *                  ....
 *      2、{@link ParameterHandler} 处理参数，属性名：parameterHandler
 *          MyBatis只给了一个默认实现{@link DefaultParameterHandler}
 *              属性：
 *                  parameterObject  存放sql语句参数
 *                  boundSql  sql语句信息
 *                  ....
 * @author liujianxin
 * @date 2018-12-08 16:30
 */
@Intercepts({@Signature(type= StatementHandler.class,method = "parameterize",args = Statement.class)})
public class UpdateParamInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("UpdateParamInterceptor-拦截到的对象："+invocation.getTarget());
        System.out.println("UpdateParamInterceptor-拦截到的方法："+invocation.getMethod());
        MetaObject metaObject = SystemMetaObject.forObject(invocation.getTarget());
        Object value = metaObject.getValue("parameterHandler.parameterObject");
        System.out.println("原参数："+value);
        //修改参数
        metaObject.setValue("parameterHandler.parameterObject",2);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
