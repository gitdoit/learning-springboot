package org.seefly.springannotation.entity;

import lombok.Data;

/**
 * 演示使用条件注解，按条件向容器中添加bean
 * @author liujianxin
 * @date 2018-12-23 15:46
 */
@Data
public class ConditionBean {
    private String systemName;
    public ConditionBean(String systemName) {
        this.systemName = systemName;
    }
}
