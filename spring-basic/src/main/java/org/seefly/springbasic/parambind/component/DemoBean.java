package org.seefly.springbasic.parambind.component;

import org.springframework.stereotype.Component;

/**
 * @author liujianxin
 * @date 2018-12-15 17:18
 */
@Component
public class DemoBean {

    public String showValue(){
        return "This value from DemoBean.showValue";
    }
}
