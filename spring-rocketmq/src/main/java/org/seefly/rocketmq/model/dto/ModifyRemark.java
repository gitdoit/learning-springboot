package org.seefly.rocketmq.model.dto;

import lombok.Data;

/**
 * @author liujianxin
 * @date 2018-07-16 13:31
 * 描述信息：
 **/
@Data
public class ModifyRemark {
    /** 微信id */
    private String wxId;
    /** 备注名 */
    private String remark;

    public ModifyRemark(String wxId, String remark) {
        this.wxId = wxId;
        this.remark = remark;
    }
}
