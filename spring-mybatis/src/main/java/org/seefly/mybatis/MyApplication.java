package org.seefly.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.seefly.mybatis.regist.MyBeanDefinitionRegister;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.io.InputStream;

/**
 * 演示Spring 整合 MyBatis的原理
 *
 * 首先入口是@Import({MyBeanDefinitionRegister.class})
 *  --> 在注册额外BeanDefinition的钩子方法里，调用自定义 MyMapperScan
 *    --> 用自定义扫描器扫面指定包下的Mapper接口
 *          --> 将接口生成的BeanDefinition替换成从SqlSession里面已经被代理的Mapper
 *              --> 再从容器中获取Mapper的时候就是被MyBatis代理的了
 *
 *
 * @author liujianxin
 * @date 2021/4/5 21:37
 */
@ComponentScan("org.seefly.mybatis")
@Import({MyBeanDefinitionRegister.class})
public class MyApplication {
    
    
    public SqlSessionFactory sqlSessionFactory() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
    
        return build;
    }
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext();
        configApplicationContext.register(MyApplication.class);
        configApplicationContext.refresh();
    
        Object userMapper = configApplicationContext.getBean("userMapper");
        System.out.println(userMapper);
    }
    
}
