package org.seefly.springannotation.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

/**
 * 演示{@link Aware}的子接口，这些接口提供了一些像是方法回调的功能
 * 能够加载一些spring底层的组件
 * 这些接口能够起作用的原因使用为BeanPostProcess的原因
 * 具体完成这项工作的是: ApplicationContextAwareProcessor
 * @author liujianxin
 * @date 2019-04-14 16:06
 */
public class AwaresDemo implements BeanNameAware, ApplicationContextAware , EmbeddedValueResolverAware {
    private ApplicationContext applicationContext;
    /**
     * 这个接口来自BeanNameAware，他的作用是
     * 如果这个类被实例化放入IOC容器，Spring会通过方法回调
     * 在放入容器之后将这个类在容器种的id返回过来。是该类本身能够感知自己被起了个啥名
     */
    @Override
    public void setBeanName(String name) {
        System.out.println("AwaresDemo 被spring起了一个:"+name);
    }
    
    /**
     * 注入上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("AwaresDemo 拿到了spring的 ApplicationContext");
        this.applicationContext = applicationContext;
    }

    /**
     * 这个可以用来解析${} 形式的表达式
     * 也可以#{}这种形式
     * 一般用来从配置文件中拿值，像是数据源的账号密码
     */
    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        System.out.println("AwaresDemo 拿到了spring的值解析器，他能够解析SpEl表达式了");
        System.out.println(resolver.resolveStringValue("${os.name}"));
    }
}
