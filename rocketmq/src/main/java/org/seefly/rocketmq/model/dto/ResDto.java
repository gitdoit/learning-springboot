package org.seefly.rocketmq.model.dto;

import lombok.Data;

/**
 * @author liujianxin
 * @date 2018-07-16 13:35
 * 描述信息：
 **/
@Data
public class ResDto {
    /**
     * "success": true,
     * "code": 200,
     * "message": "操作成功！",
     * "data": {
     *     "cmdId": "2018062116172091",//指令ID
     *     "msgId": "C0A805E368CC18B4AAC267EADD050000"//消息ID(仅作参考)
     *  }
     */
    private boolean success;
    private Integer code;
    private String message;
    private RespData data;

    @Data
    public static class RespData {
        private String cmdId;
        private String msgId;
    }
}
