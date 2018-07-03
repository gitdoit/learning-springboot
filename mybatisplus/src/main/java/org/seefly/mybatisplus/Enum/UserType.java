package org.seefly.mybatisplus.Enum;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;

/**
 * @author liujianxin
 * @date 2018/7/3 17:15
 * 描述：
 */
public enum UserType implements IEnum {
    USER(1),
    MANANGER(2),
    SUPER_ADMAIN(3)
    ;
    private final Integer type;
    UserType(Integer type){
        this.type = type;
    }


    /**
     * 通过
     * @param type
     * @return
     */
    public static UserType getEnumByType(Integer type){
        for(UserType e : UserType.values()){
            if(e.type == type){
                return e;
            }
        }
        return null;
    }


    @Override
    public Serializable getValue() {
        return this.type;
    }
}
