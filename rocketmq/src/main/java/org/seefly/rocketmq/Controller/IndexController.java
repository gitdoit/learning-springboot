package org.seefly.rocketmq.Controller;

import org.seefly.rocketmq.Event.MyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujianxin
 * @date 2018-07-12 16:30
 * 描述信息：
 **/
@RestController
public class IndexController {
    @Autowired
    ApplicationEventPublisher eventPublisher;


    /**
     * 事件监听测试，发布的事件要被监听到需要发布和监听器一样的事件类型
     * @param str
     * @return
     */
    @RequestMapping("/test")
    public String index(String str){
        eventPublisher.publishEvent(new MyEvent("sdfsdf",str));
        return "ok";
    }
}
