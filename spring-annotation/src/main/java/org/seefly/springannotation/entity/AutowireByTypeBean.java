package org.seefly.springannotation.entity;

/**
 * 用来演示使用BeanDefinition注入注册表
 * 然后给这个BeanDefinition设置注入模式为byName模式。看看效果
 * @author liujianxin
 * @date 2021/4/6 21:27
 */
public class AutowireByTypeBean {
    private Person person;
    
    public void setPerson(Person person){
        System.out.println("AutowireByNameBean被容器自动注入依赖了，是byName模式");
        this.person = person;
    }
    
}
