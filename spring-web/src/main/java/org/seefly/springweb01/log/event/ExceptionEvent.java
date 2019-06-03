package org.seefly.springweb01.log.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @author liujianxin
 * @date 2019-04-25 14:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class ExceptionEvent extends ApplicationEvent {


    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ExceptionEvent(Object source) {
        super(source);
    }
}
