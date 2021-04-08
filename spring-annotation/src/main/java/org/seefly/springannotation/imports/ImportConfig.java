package org.seefly.springannotation.imports;

import org.seefly.springannotation.imports.registrar.MyImportBeanDefinitionRegistrar;
import org.seefly.springannotation.imports.selector.MyImportSelector;
import org.seefly.springannotation.process.factory.BeanFactoryPostProcessDemo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;

/**
 *
 *
 * 用来演示 @Import 注解，使用方法
 *
 * 配置类注解会生效的原因在于 {@link ConfigurationClassPostProcessor}(这个类生效的原因详见{@link BeanFactoryPostProcessDemo}) Bean工厂初始化的后置处理器
 *
 * 原理：
 *   --> 生命周期的 postProcessBeanFactory 方法
 *      --> 调用bean工厂的后置处理器 {@link ConfigurationClassPostProcessor}
 *        --> 处理 @Import注解
 *
 *
 * {@link Import}用于根据传入的参数向容器中添加指定组件
 *  1、参数直接指定类型，即直接注入这个Bean
 *  2、参数指定一个{@link ImportSelector}的实例，则会根据接口返回值向容器添加组件
 *  3、参数指定一个{@link ImportBeanDefinitionRegistrar}的实例，自定义手动注册组件
 */
@Configuration
//@Import({ImportBean.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
public class ImportConfig {





}
