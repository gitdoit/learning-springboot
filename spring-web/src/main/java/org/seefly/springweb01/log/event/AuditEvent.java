package org.seefly.springweb01.log.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @author liujianxin
 * @date 2019-04-25 14:31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class AuditEvent extends ApplicationEvent {
    private Integer userId;
    private String ip;
    private String platform;
    private String loginName;
    private String userName;
    private String funcName;
    private String params;
    private String resultData;
    private Date createTime;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AuditEvent(Object source) {
        super(source);
        this.createTime = new Date();
    }
}
