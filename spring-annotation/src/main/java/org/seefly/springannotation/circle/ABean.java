package org.seefly.springannotation.circle;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 演示循环依赖，看看三级缓存
 * @author liujianxin
 * @date 2021/4/7 10:01
 */
@Component
public class ABean {
    private BBean bBean;
    
    public ABean(@Lazy BBean bBean) {
        this.bBean = bBean;
    }
}
