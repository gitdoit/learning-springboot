package org.seefly.springbasic.customize.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;


@Slf4j
public class RedisListenerMethodAdapter  implements GenericApplicationListener {
    private final ResolvableType supportEventType;
    private final Method method;
    private final Class<?> targetClass;
    private final String beanName;
    private final  ApplicationContext applicationContext;

    public RedisListenerMethodAdapter(String beanName, Class<?> targetClass, Method method,ApplicationContext applicationContext) {
        if(method.getParameterCount() != 1) {
            throw new IllegalArgumentException("@RedisMessageListener 方法必须有且只有一个参数:"+method);
        }
        this.supportEventType = ResolvableType.forMethodParameter(method, 0);
        this.method = method;
        this.targetClass = targetClass;
        this.beanName = beanName;
        this.applicationContext = applicationContext;
    }


    /**
     * 简单支持自定义参数
     */
    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        Class<?> eventClass = eventType.getRawClass();
        // 只支持 PayloadApplicationEvent<Foo>
        if (eventClass != null && PayloadApplicationEvent.class.isAssignableFrom(eventClass)) {
            ResolvableType payloadType = eventType.as(PayloadApplicationEvent.class).getGeneric();
            return supportEventType.isAssignableFrom(payloadType);
        }
        return false;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        PayloadApplicationEvent<?> payloadEvent = (PayloadApplicationEvent<?>) event;
        Object[] args = new Object[]{payloadEvent.getPayload()};
        Object bean = getTargetBean();
        ReflectionUtils.makeAccessible(this.method);
        try {
            // 不应该 也不处理返回值
            this.method.invoke(bean, args);
        }
        catch (Exception ex) {
            log.error("@RedisMessageListener 引用错误！",ex);
        }
    }


    protected Object getTargetBean() {
        Assert.notNull(this.applicationContext, "ApplicationContext must no be null");
        return this.applicationContext.getBean(this.beanName);
    }
}
