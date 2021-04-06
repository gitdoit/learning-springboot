package org.seefly.mybatis.scan;

import org.seefly.mybatis.factory.MapperFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

/**
 * @author liujianxin
 * @date 2021/4/5 21:37
 */
public class MyMapperScan extends ClassPathBeanDefinitionScanner {
    
    public MyMapperScan(BeanDefinitionRegistry registry) {
        super(registry);
        this.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
    }
    
    
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
    
        for (BeanDefinitionHolder holder : beanDefinitionHolders) {
            BeanDefinition beanDefinition = holder.getBeanDefinition();
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
            beanDefinition.setBeanClassName(MapperFactoryBean.class.getName());
    
        }
        
        
        return beanDefinitionHolders;
    }
    
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
       return  beanDefinition.getMetadata().isInterface();
    }
}
