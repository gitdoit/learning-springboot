package org.seefly.rocketmq.Event;

import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author liujianxin
 * @date 2018-07-12 16:37
 * 描述信息：
 **/
@Getter
public class MyEvent extends ApplicationEvent {
    private String msg;

    public MyEvent(Object source) {
        super(source);
    }

    public MyEvent(Object source,String msg){
        super(source);
        this.msg = msg;
    }
}
