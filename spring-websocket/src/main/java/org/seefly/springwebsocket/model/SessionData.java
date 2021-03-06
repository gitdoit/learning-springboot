package org.seefly.springwebsocket.model;

import lombok.Data;

/**
 * @author liujianxin
 * @date 2019-02-25 17:59
 */
@Data
public class SessionData {
    /**命令类型*/
    private String type;
    /**话术模板id*/
    private String robotId;
    /**语音网关会话ID*/
    private String sessionId;
}
