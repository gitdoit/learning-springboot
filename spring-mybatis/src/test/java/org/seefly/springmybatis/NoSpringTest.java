package org.seefly.springmybatis;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.executor.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.*;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.seefly.springmybatis.dao.Demo;
import org.seefly.springmybatis.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * {@link SqlSessionFactory}的生命周期和应用相同，作用于应为整个应用，且为单例。
 * {@link SqlSession}不是线程安全，的最佳作用于为方法。但如果为Web应用则考虑放在和HTTP请求类似的作用域中，这样在一次请求/响应中
 * 可以使用这一个会话；会话在使用完毕后应该主动被关闭。
 * SqlSessionFactory的创建
 *    1、首先会根据传入的MyBatis的配置文件生成一个{@link XMLConfigBuilder},然后使用这个类对configuration节点进行解析
 *    {@link XMLConfigBuilder#parseConfiguration(org.apache.ibatis.parsing.XNode)}
 *    2、将所有子节点的属性全部解析完成之后放入{@link Configuration}中，其中在解析到mapper子节点的时候会进入到这个方法
 *       {@link XMLConfigBuilder#mapperElement(org.apache.ibatis.parsing.XNode)}
 *    3、这个方法用来解析Mapper.xml文件，他会调用{@link XMLMapperBuilder#parse()}方法来将这个mapper文件的所有节点进行解析
 *    4、在解析mapper文件的时候，最主要的是{@link XMLMapperBuilder#buildStatementFromContext(java.util.List, java.lang.String)}方法
 *       它将解析文件中定义的所有增删改查的sql语句节点，并将这些节点使用{@link XMLStatementBuilder#parseStatementNode()}进行解析
 *    5、最终这些解析出来的数据都会放入{@link Configuration}中，并使用它作为参数生成一个{@link DefaultSqlSessionFactory}返回
 *
 *
 *  比较重要的是，这些解析完成之后会将sql语句放在Configuration的mappedStatements属性中,这个属性是一个map，值为sql的id，value就是sql语句的包装{@link MappedStatement}
 *  其中还有一个属性为mapperRegistry{@link MapperRegistry}，这里面缓存了mapper接口代理对象
 *
 *
 * SqlSession的获取
 *   1、{@link SqlSessionFactory#openSession()}空参获取SqlSession，从上一步可以直到，这里的SqlSessionFactory的实际类型是DefaultSqlSessionFactory，所以
 *   在这个实现类中的这个实现方法上实际上调用的是{@link DefaultSqlSessionFactory#openSessionFromDataSource(org.apache.ibatis.session.ExecutorType, org.apache.ibatis.session.TransactionIsolationLevel, boolean)}方法
 *   第一个参数传入的是默认的执行类型{@link ExecutorType#SIMPLE},没有传入隔离级别，以及自动提交（默认false）
 *   2、根据configuration创建事务
 *   3、调用{@link Configuration#newExecutor(org.apache.ibatis.transaction.Transaction, org.apache.ibatis.session.ExecutorType)}方法创建执行器，第一个参数为上一步创建的事务
 *      第二个参数为传入的执行器类型，这里为Simple类型。
 *   4、在创建执行器的方法中，首先根据执行器类型选择不同的实现类，有三个{@link BatchExecutor}批处理的 {@link ReuseExecutor}可复用的 {@link SimpleExecutor}简单的
 *      这里创建一个简单的执行器，然后根据Configuration中的配置，选择是否使用二级缓存类{@link CachingExecutor}对执行器进行包装。
 *      最后一步则是{executor = (Executor) interceptorChain.pluginAll(executor);}将所有拦截器插件注入这个执行器(如果有)
 *
 *  Mapper的获取
 *    1、SqlSession.getMapper(Class)，由上述步骤可知，这里的SqlSession实际类型为DefaultSqlSession，然而他里面也是直接调用{@link Configuration#getMapper(java.lang.Class, org.apache.ibatis.session.SqlSession)}
 *    2、Configuration里面是直接调用{@link MapperRegistry#getMapper(java.lang.Class, org.apache.ibatis.session.SqlSession)},这个MapperRegistry上面已经见到过，它里面
 *       存储了接口和Mapper代理工厂的映射关系，所以这个方法中通过你传入的class对象，获取{@link MapperProxyFactory}，并调用他的newInstance方法
 *    3、{@link MapperProxyFactory#newInstance(org.apache.ibatis.session.SqlSession)}方法中创建了一个{@link MapperProxy}，它是Mapper接口的代理，这也就是为什么只有接口
 *      没有实现类也能够调用，这个代理实现了{@link InvocationHandler}JDK提供的接口，最终调用JDK的{@link Proxy#newProxyInstance(java.lang.ClassLoader, java.lang.Class[], java.lang.reflect.InvocationHandler)}
 *      方法，实现代理。
 *
 *  调用Mapper方法查询
 *   1、直接调用Mapper接口的方法，会被MapperProxy拦截到，拦截到之后根据所调用的方法创建{@link MapperMethod}或从缓存取
 *   2、调用{@link MapperMethod#execute }方法，它根据MappedStatement判断这个SQL的类型(增删改查),调用SqlSession的不同方法执行Sql语句，这里演示查询，所有查询操作最后调用的都是selectList
 *   3、DefaultSqlSession中的查询方法(selectList)实际上调用的是{@link BaseExecutor#query}（若开启了二级缓存则从缓存中获取）
 *   3、最后调用{@link BaseExecutor#queryFromDatabase}
 *
 * @author liujianxin
 * @date 2018-12-05 13:08
 */
public class NoSpringTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void before() throws IOException {
        InputStream resource = Resources.getResourceAsStream("configuration.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(resource);
    }

    @Test
    public void testRun(){
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            User o = sqlSession.selectOne("org.seefly.springmybatis.dao.Demo.selectById", 1);
            System.out.println(o);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMapper(){
        try(SqlSession sqlSession =sqlSessionFactory.openSession()){
            Demo mapper = sqlSession.getMapper(Demo.class);
            User user = mapper.selectById(1);
            System.out.println(user);
        }
    }


    @Test
    public void testInsert(){
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.insert("org.seefly.springmybatis.dao.Demo.insert", new User(1,"sdfsdf"));
            // 对于mybatis自身的事务管理，如果在更新操作之后不主动提交，则会回滚。
            // 若使用其他框架(spring)来管理mybatis的事务，那么这些例如手动提交、回滚等操作都无效。它们将被框架所管理
            sqlSession.commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
