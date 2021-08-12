package org.seefly.springbasic.customize.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liujianxin
 */
@Slf4j
public class RedisEventMethodProcessor implements SmartInitializingSingleton, ApplicationContextAware , BeanFactoryPostProcessor {
    private ConfigurableApplicationContext applicationContext;
    private ConfigurableListableBeanFactory beanFactory;
    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(8));


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    /**
     * 扫描容器中所有的bean实例
     * 带有RedisMessageListener注解的方法抽出来包装成监听器注册到容器上
     */
    @Override
    public void afterSingletonsInstantiated() {
        ConfigurableListableBeanFactory beanFactory = this.beanFactory;
        Assert.state(this.beanFactory != null, "No ConfigurableListableBeanFactory set");
        String[] beanNames = beanFactory.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            if (!ScopedProxyUtils.isScopedTarget(beanName)) {
                Class<?> type = AutoProxyUtils.determineTargetClass(beanFactory, beanName);
                if (type != null) {
                    if (ScopedObject.class.isAssignableFrom(type)) {
                        Class<?> targetClass = AutoProxyUtils.determineTargetClass( beanFactory, ScopedProxyUtils.getTargetBeanName(beanName));
                        if (targetClass != null) {
                            type = targetClass;
                        }
                    }
                    processBean(beanName, type);
                }
            }
        }


    }

    private void processBean(final String beanName, final Class<?> targetType) {
        if (!this.nonAnnotatedClasses.contains(targetType) &&
                !targetType.getName().startsWith("java") &&
                !isSpringContainerClass(targetType)) {

            Map<Method, RedisMessageListener> annotatedMethods = null;
            try {
                annotatedMethods = MethodIntrospector.selectMethods(targetType,
                        (MethodIntrospector.MetadataLookup<RedisMessageListener>) method ->
                                AnnotatedElementUtils.findMergedAnnotation(method, RedisMessageListener.class));
            }
            catch (Throwable ex) {
                // An unresolvable type in a method signature, probably from a lazy bean - let's ignore it.
                if (log.isDebugEnabled()) {
                    log.debug("Could not resolve methods for bean with name '" + beanName + "'", ex);
                }
            }

            if (CollectionUtils.isEmpty(annotatedMethods)) {
                this.nonAnnotatedClasses.add(targetType);
                if (log.isTraceEnabled()) {
                    log.trace("No @EventListener annotations found on bean class: " + targetType.getName());
                }
            }
            else {
                // Non-empty set of methods
                ConfigurableApplicationContext context = this.applicationContext;
                Assert.state(context != null, "No ApplicationContext set");
                for (Method method : annotatedMethods.keySet()) {
                    Method methodToUse = AopUtils.selectInvocableMethod(method, context.getType(beanName));
                    RedisListenerMethodAdapter listener = new RedisListenerMethodAdapter(beanName,targetType,methodToUse,context);
                    context.addApplicationListener(listener);
                }
                
            }
        }
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Assert.isTrue(applicationContext instanceof ConfigurableApplicationContext,
                "ApplicationContext does not implement ConfigurableApplicationContext");
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    private static boolean isSpringContainerClass(Class<?> clazz) {
        return (clazz.getName().startsWith("org.springframework.") &&
                !AnnotatedElementUtils.isAnnotated(ClassUtils.getUserClass(clazz), Component.class));
    }
}
