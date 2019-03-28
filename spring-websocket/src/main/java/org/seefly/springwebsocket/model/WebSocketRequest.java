package org.seefly.springwebsocket.model;

import lombok.Data;

/**
 * @author liujianxin
 * @date 2019-02-28 20:29
 */
@Data
public class WebSocketRequest {
    /**命令类型:init sessionId end*/
    private String type;
    /**话术模板id*/
    private String robotId;
    /**语音网关会话ID*/
    private String sessionId;
}

