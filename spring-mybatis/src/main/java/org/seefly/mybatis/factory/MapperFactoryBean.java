package org.seefly.mybatis.factory;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

/**
 * @author liujianxin
 * @date 2021/4/5 21:46
 */
public class MapperFactoryBean implements FactoryBean{
    private Class clazz;
    private SqlSession session;
    
    public MapperFactoryBean(Class clazz) {
        this.clazz = clazz;
    }
    
    
    public void setSession(SqlSessionFactory session) {
        session.getConfiguration().addMapper(clazz);
        this.session = session.openSession();
    }
    
    @Override
    public Object getObject() throws Exception {
        // mybatis整合spring是通过 sqlSession.getMapper();来的
        // 但是要配置文件，就没弄，如果配了 下面的动态代理 就删掉了，直接用sqlSession.getMapper(clazz)就行了
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), clazz.getInterfaces(), (proxy, method, args) -> {
    
            System.out.println("代理对象");
            
            if("toString".equals(method.getName())){
                return "这是代理类："+clazz.getName();
            }
            
            return null;
        });
    }
    
    @Override
    public Class<?> getObjectType() {
        return clazz;
    }
}
