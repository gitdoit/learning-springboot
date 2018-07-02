package org.seefly.bmybatis;

import org.junit.Test;
import org.seefly.bmybatis.Enum.SimpleEnum;

/**
 * @author liujianxin
 * @date 2018-07-02 23:28
 * 描述信息：
 **/
public class NoSpringTest {

    @Test
    public void testSmpileEnum(){
        for(SimpleEnum e : SimpleEnum.values()){
            System.out.println("int Enum.ordinal:"+e.ordinal());
            System.out.println("int Enum.compareTo(Enum):"+e.compareTo(SimpleEnum.B));
            System.out.println("Enum.getDeclaringClss():"+e.getDeclaringClass());
            System.out.println("Enum.name():"+e.name());
            System.out.println("=================================================");
        }
    }
}
