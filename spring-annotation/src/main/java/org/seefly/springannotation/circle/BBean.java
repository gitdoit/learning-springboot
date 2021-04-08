package org.seefly.springannotation.circle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 演示循环依赖，看看三级缓存
 * @author liujianxin
 * @date 2021/4/7 10:01
 */
@Component
public class BBean {
    private ABean aBean;
    
    public BBean(ABean aBean) {
        this.aBean = aBean;
    }
}
