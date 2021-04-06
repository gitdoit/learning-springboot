package org.seefly.springannotation.condition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author liujianxin
 * @date 2021/4/5 17:51
 */
@Configuration
public class ConditionConfig {
    
    /**
     * 根据自定义条件向容器中添加实例
     */
    @Conditional({OperatingSystemCondition.class})
    @Bean
    public ConditionBean conditionBean(){
        System.out.println("创建windows实例....");
        return new ConditionBean("windows 10");
    }
    
}
