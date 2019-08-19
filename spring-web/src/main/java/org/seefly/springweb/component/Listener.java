package org.seefly.springweb.component;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

/**
 * @author liujianxin
 * @date 2019-08-19 11:36
 */
@Component
public class Listener implements ApplicationListener<ServletRequestHandledEvent> {
    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        System.out.println(event);
    }
}
