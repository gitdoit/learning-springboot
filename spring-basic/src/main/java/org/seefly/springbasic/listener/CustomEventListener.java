package org.seefly.springbasic.listener;

import lombok.extern.slf4j.Slf4j;
import org.seefly.springbasic.customize.listener.RedisMessageListener;
import org.seefly.springbasic.event.CustomEvent;
import org.seefly.springbasic.event.RedisEvent;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.EventListenerMethodProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *  【实现】
 *      {@link EventListener} 的功能实现于 {@link EventListenerMethodProcessor}，很明显是一个BeanFactoryPostProcessor；
 *      整个实现逻辑很简单， 由于{@link EventListenerMethodProcessor}也实现了{@link SmartInitializingSingleton}接口
 *      所以在所有单例bean处理完成之后会回调这个接口，在这个接口里面扫描所有bean实例的方法，把标注了{@link EventListener}的方法抽出来
 *      包装成为{@link ApplicationListener},然后 context.addApplicationListener(applicationListener);
 *
 *  【使用】
 * 引用通过Bean的方式来注册监听器，所以肯定的是在容器初始化完成前有些事件你是接不到的
 * 所以可以在构建容器前手动配置一些监听器
 * 1、例如编码方式的
 *  SpringApplicationBuilder builder = new SpringApplicationBuilder(SpringBasicApplication.class);
 *         builder.listeners((ApplicationListener< ApplicationReadyEvent>) event -> {
 *             // do something
 *         });
 *         builder.build().run(args);
 *
 *
 * 2、也可以按照约定，在 META-INF/spring.factories下添加监听器
 *      org.springframework.context.ApplicationListener=com.example.project.MyListener
 *
 * 事件类型：
 * 1、ApplicationStartingEvent，程序启动事件
 * 2、ApplicationEnvironmentPreparedEvent ，jvm变量和系统环境变量
 * 3、ApplicationContextInitializedEvent
 * 4、ApplicationPreparedEvent
 * 5、ApplicationStartedEvent
 * 6、ApplicationReadyEvent，容器可用，在CommandLineRunner之后调用
 * 7、WebServerInitializedEvent web环境下，环境准备好之后的事件
 *
 * 顺序消费：
 *  支持注解方式标注监听器顺序和接口继承方式
 *
 * 异步支持
 *  开启@EnableAsync注解，然后在监听器方法上标注@Async
 *
 * @author liujianxin
 * @date 2021/4/8 10:18
 */
@Slf4j
@Component
public class CustomEventListener implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    // 监听容器事件
    @EventListener
    // 还支持顺序消费
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void listener(ApplicationReadyEvent event){
        System.out.println("@EventListener回调");
        // 发送另一个事件
        CustomEvent e = new CustomEvent();
        e.setMsg("hello world");
        applicationEventPublisher.publishEvent(e);
        applicationEventPublisher.publishEvent(new RedisEvent("88888"));
    }
    
    
    @EventListener
    public void listenerSelf(CustomEvent payload){
        log.info("[自定义事件]{}",payload);
    }

    @RedisMessageListener(topic = "ahaha")
    public void redis(RedisEvent event){
        log.info("[自定义Redis事件]{}",event);
    }

    
    
    
    
    
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
