package org.seefly.mybatisplus.Enum;

import lombok.Getter;

/**
 * @author liujianxin
 * @date 2018-07-02 23:54
 * 描述信息：
 **/
@Getter
public enum MethodEnum {
    //注意，如果定义自己的方法，那么要在最后一个枚举后加分号。
    A("这是A"),B("这是B"),C("这是C");
    private final String desc;
    MethodEnum (String desc){
        this.desc = desc;
    }

    public static  void main(String[] args){
        for(MethodEnum e : MethodEnum.values()){
            System.out.println(e+":"+e.getDesc());
        }
    }


}
