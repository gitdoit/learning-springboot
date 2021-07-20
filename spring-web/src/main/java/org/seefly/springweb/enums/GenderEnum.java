package org.seefly.springweb.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 性别枚举
 * @author liujianxin
 * @date 2021/7/19 16:57
 **/
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GenderEnum {

    /**
     * 男
     */
    MALE(1),

    /**
     * 女
     */
    FEMALE(0);



    private final int value;

    GenderEnum( int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
