package org.seefly.springbasic.parambind.beans;

import lombok.Data;

/**
 * @author liujianxin
 * @date 2018-08-03 16:42
 * 描述信息：该bean用来演示使用Binder来绑定属性，代替之前的@ConfigurationProperties(prefix = "")
 **/
@Data
public class BinderBean {
    private String name;
    private String add;
}
