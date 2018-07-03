package org.seefly.mybatisplus;

import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author liujianxin
 * @date 2018/7/3 13:09
 * 描述：
 */
public class NoSpringTest {

    @Test
    public void testMethod(){
        List<Integer> list = new ArrayList<>();
        Integer a = new Integer(5);
        a.compareTo(3);
        //原始用法
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return 0;
            }
        });
        // lambda表达式用法
        Collections.sort(list,(c,b) -> c.compareTo(b));
        // 另一种类似于 a.compareTo(3); object.instanceMathid
        Collections.sort(list,Integer::compareTo);
        // forEach需要传入接口参数只有一个accept(Integer integer)
        list.forEach(d-> System.out.println(d));
        // 调用类的静态方法，这个方法的形参数量以及类型必须和接口一样
        list.forEach(System.out::print);



    }
}
