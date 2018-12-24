package org.seefly.springannotation.selector;

import org.seefly.springannotation.entity.ImportRegistrarBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author liujianxin
 * @date 2018-12-23 19:08
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     *
     * @param importingClassMetadata 引用处注解信息
     * @param registry bean的注册接口
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean person = registry.containsBeanDefinition("person");
        boolean scopeBean = registry.containsBeanDefinition("scopeBean");
        // 如果这两个组件都在容器中，那么注册一个组件
        if(person && scopeBean){
            // bean的定义信息
            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(ImportRegistrarBean.class);
            registry.registerBeanDefinition("seefly",rootBeanDefinition);
        }
    }
}
