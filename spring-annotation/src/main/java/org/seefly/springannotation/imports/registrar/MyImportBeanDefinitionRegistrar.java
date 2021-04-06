package org.seefly.springannotation.imports.registrar;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 配合@import 注解，将当前类当作参数
 * 这样Spring会在bean工厂的后置钩子方法中调用 registerBeanDefinitions 方法
 * 我们可以在这个方法里自定义的向容器中注入 BeanDefinition信息
 *
 * 生效原因:
 *  --> 生命周期的 postProcessBeanFactory 方法
 *     --> 调用bean工厂的后置处理器 {@link ConfigurationClassPostProcessor}
 *       --> 处理 @Import注解
 *        --> 调用registerBeanDefinitions方法向容器中注入额外的BeanDefinition
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
    
    public static class ImportRegistrarBean{
    
        public ImportRegistrarBean() {
            System.out.println("@Import+ImportBeanDefinitionRegistrar注入的Bean：ImportRegistrarBean");
        }
    }
}
