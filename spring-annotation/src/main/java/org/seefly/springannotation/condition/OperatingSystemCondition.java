package org.seefly.springannotation.condition;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * 演示条件注解{@link Conditional}配合自定义{@link Condition}实现类
 * 按条件添加实例到容器
 * @author liujianxin
 * @date 2018-12-23 15:51
 */
public class OperatingSystemCondition implements Condition {
    /**
     *
     * @param context 上下文
     * @param metadata 注解信息
     * @return 满足条件返回true，不满足返回false
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取运行环境
        Environment environment = context.getEnvironment();
        // 获取类加载器
        ClassLoader classLoader = context.getClassLoader();
        // 获取bean定义注册
        BeanDefinitionRegistry registry = context.getRegistry();
        // 获取bean工厂
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        //获取操作系统的名称
        String property = environment.getProperty("os.name");
        return !StringUtils.isEmpty(property) && property.contains("Windows");
    }
}
