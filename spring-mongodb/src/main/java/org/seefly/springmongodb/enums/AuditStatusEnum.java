package org.seefly.springmongodb.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author liujianxin
 * @date 2021/7/19 15:53
 **/
public enum AuditStatusEnum {

    /**
     * 审核通过
     */
    PASSED(1),

    /**
     * 审核中
     */
    AUDITING(0),

    /**
     * 审核不通过
     */
    NOT_PASSED(-1);

    private final int value;

    AuditStatusEnum( int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
