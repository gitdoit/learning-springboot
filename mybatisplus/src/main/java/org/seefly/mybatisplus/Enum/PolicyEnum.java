package org.seefly.mybatisplus.Enum;

/**
 * @author liujianxin
 * @date 2018/7/3 16:50
 * 描述：
 */
public enum PolicyEnum {
    MOBILE("mobile"),SERVICE("service");
    private final String value;
    PolicyEnum(String value){
        this.value = value;
    }

    /**
     * 通过字符串获取对应的枚举类型，以此限定取值
     * 相当于数据库中的字典表，限定你对于某些字段只能传入指定的值
     * @param value
     * @return
     */
    public static PolicyEnum getValue(String value){
        for(PolicyEnum e : PolicyEnum.values()){
            if(e.value.equals(value)){
                return e;
            }
        }
        return null;
    }

    public static void main(String[] args){
        PolicyEnum service = PolicyEnum.getValue("service");
        System.out.println();
    }

}
