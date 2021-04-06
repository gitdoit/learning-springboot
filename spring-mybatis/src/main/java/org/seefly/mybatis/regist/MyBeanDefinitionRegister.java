package org.seefly.mybatis.regist;

import org.seefly.mybatis.scan.MyMapperScan;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author liujianxin
 * @date 2021/4/5 22:07
 */
public class MyBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {
    
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        MyMapperScan scan = new MyMapperScan(registry);
        scan.scan("org.seefly.mybatis.mapper");
    }
}
