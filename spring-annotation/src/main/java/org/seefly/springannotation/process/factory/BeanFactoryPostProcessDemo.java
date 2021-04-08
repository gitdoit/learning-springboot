package org.seefly.springannotation.process.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * 主要作用：
 *  根据注释文档说明，这个接口主要是用来修改BeanDefinition中的相关信息
 *  例如实现类{@link PropertySourcesPlaceholderConfigurer} 就是用来修改@Value("${xxx}")中占位符的值的
 *
 * 调用链：
 *  -->refresh
 *    --> invokeBeanFactoryPostProcessors
 *      --> PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors
 *      调用所有的BeanFactoryPostProcessor.postProcessBeanFactory方法
 *
 *
 * 【！！！！！！！！！！！！！重要！！！！！！！！！！！！！！！！】
 * 其中子接口 {@link BeanDefinitionRegistryPostProcessor}扩展了父接口
 * 【！！！！！！！！！！！！！重要！！！！！！！！！！！！！！！！】
 * 使他能够向容器中注入额外的BeanDefinition
 * 例如：
 * 1、主要子类{@link ConfigurationClassPostProcessor}
 *  它的任务如下
 *      1. 处理PropertySources 注解加载property资源
 *      2. 处理ComponentScans和ComponentScan注解
 *           扫描指定包下所有@Component注解。包括@Service，@Controller,@Repository,@Configuration,@ManagedBean
 *      3. 处理@Import注解类 解析所有包含@Import注解。spring中@Enable****注解的实现依赖。@Import的value 有两种类型的Class.
 *          3.1 ImportSelector 导入选择器。
 *               执行非DeferredImportSelector接口的，收集配置类。
 *               收集DeferredImportSelector接口的实现。稍后执行。
 *           3.2 ImportBeanDefinitionRegistrar 导入类定义注册者
 *               收集ImportBeanDefinitionRegistrar对象。
 *           3.3 如果不是上面两种类型 就会被当作普通的configuration 类 注册到容器，例如@EnableScheduling
 *      4. 处理ImportResource注解，收集@ImportResource中指定的配置文件信息。
 *      5. 收集@Bean注解的方法
 *      6. 处理接口默认方法
 *      7. 处理父类，父类className 不是"java"开头，并且是未处理过的类型。获取父类循环1-7。
 *      8. 处理3.1中收集的DeferredImportSelector。
 *      9. 利用ConfigurationClassBeanDefinitionReader 处理每个Configuration类的@Bean和第4步收集的资源和3.2中收集的注册者。
 *
 * 2、主要子类MapperScannerConfigurer，就是Spring整合MyBatis时，向容器中注入被MyBatis代理的Mapper接口的
 *
 * @author liujianxin
 * @date 2021/4/5 20:39
 */
public class BeanFactoryPostProcessDemo implements BeanFactoryPostProcessor {
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}
