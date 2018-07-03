package org.seefly.mybatisplus.Enum;

/**
 * @author liujianxin
 * @date 2018-07-02 23:21
 * 描述信息：学习使用枚举类型
 **/
public enum SimpleEnum {
    A,B,C;
    public static void main(String[] arts){
        for(SimpleEnum e : SimpleEnum.values()){
            System.out.println("int Enum.ordinal:"+e.ordinal());
            System.out.println("int Enum.compareTo(Enum):"+e.compareTo(SimpleEnum.B));
            System.out.println("Enum.getDeclaringClss():"+e.getDeclaringClass());
            System.out.println("Enum.name():"+e.name());
            System.out.println("=================================================");
        }
    }
}
