package org.seefly.springannotation.config;

import org.seefly.springannotation.condition.OperatingSystemCondition;
import org.seefly.springannotation.entity.*;
import org.seefly.springannotation.selector.MyImportBeanDefinitionRegistrar;
import org.seefly.springannotation.selector.MyImportSelector;
import org.springframework.context.annotation.*;

/**
 *
 * 配置类注解会生效的原因在于->ConfigurationClassPostProcessor,Bean工厂初始化的后置处理器
 * 1. 处理PropertySources 注解
 * 加载property资源
 * 2. 处理ComponentScans和ComponentScan注解
 * 扫描指定包下所有@Component注解。包括@Service，@Controller,@Repository,@Configuration,@ManagedBean
 * 3. 处理@Import注解类
 *
 * 解析所有包含@Import注解。spring中@Enable****注解的实现依赖。@Import的value 有两种类型的Class.
 * 3.1 ImportSelector 导入选择器。
 * 执行非DeferredImportSelector接口的，收集配置类。
 * 收集DeferredImportSelector接口的实现。稍后执行。
 * 3.2 ImportBeanDefinitionRegistrar 导入类定义注册者
 * 收集ImportBeanDefinitionRegistrar对象。
 * 3.3 如果不是上面两种类型 就会被当作普通的configuration 类 注册到容器。
 * 例如@EnableScheduling
 * 4. 处理ImportResource注解
 * 收集@ImportResource中指定的配置文件信息。
 * 5. 收集@Bean注解的方法
 *
 * 6. 处理接口默认方法
 * 7. 处理父类
 * 父类className 不是"java"开头，并且是未处理过的类型。获取父类循环1-7。
 * 8. 处理3.1中收集的DeferredImportSelector。
 * 9.利用ConfigurationClassBeanDefinitionReader 处理每个Configuration类的@Bean和第4步收集的资源和3.2中收集的注册者。
 *
 *
 *
 *
 *
 *
 * {@link Import}用于根据传入的参数向容器中添加指定组件
 *  1、参数直接指定类型
 *  2、参数指定一个{@link ImportSelector}的实例，则会根据接口返回值向容器添加组件
 *  3、参数指定一个{@link ImportBeanDefinitionRegistrar}的实例，自定义手动注册组件
 */
@Configuration
@Import({ImportBean.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
public class ImportConfig {

    @Bean
    public Person person(){
        return new Person("liu",12);
    }


    /**
     * 默认为单实例的。
     * prototype 多实例的，在多实例情况下，为懒加载，即在获取这里实例的时候才会创建一个。
     * singleton 单实例的，在单实例情况下，在容器初始化的时候会创建单实例放入容器。
     *  {@link Lazy},对于单实例的情况下，容器在初始化的时候不会创建单实例，只有在第一次调用的时候次啊会创建
     * request mvc环境下 一个请求一个实例
     * session mvc环境下，一次会话 一个实例
     */
    @Lazy
    @Scope("singleton")
    @Bean
    public ScopeBean scopeBean(){
        System.out.println("创建scopeBean实例...........");
        return new ScopeBean();
    }


    /**
     * 根据自定义条件向容器中添加实例
     */
    @Conditional({OperatingSystemCondition.class})
    @Bean
    public ConditionBean conditionBean(){
        System.out.println("创建windows实例....");
        return new ConditionBean("windows 10");
    }

    /**
     * 向容其中添加该FactoryBean产生的实例(Person)
     * 该FactoryBean实际的作用就是生产Person实例。
     */
    @Bean
    public PersonFactoryBean personFactoryBean(){
        return new PersonFactoryBean();
    }


}
