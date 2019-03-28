package org.seefly.springwebsocket.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujianxin
 * @date 2019-02-28 20:32
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebSocketResponse {
    /**
     * 消息类型:
     *  robot ：机器人说话翻译
     *  custom：客户说话翻译
     *  init  : 初始化命令响应
     *  exception: 命令执行异常
     *  end：训练结束
     *
     *  */
    private String type;
    /**消息内容*/
    private String content;
    /**匹配信息*/
    private String matchInfo;
    /**状态*/
    private String status;
}
