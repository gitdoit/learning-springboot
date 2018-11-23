package org.seefly.lambda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author liujianxin
 * @date 2018-11-22 19:32
 */
public class ComparatorDemo {

    public static void main(String[] args){
        // 声明式编程构造一个比较器，先按字符长度排序，再按大小写不敏感字符排序
        Comparator<String> comparator = Comparator.comparingInt(String::length).thenComparing(String.CASE_INSENSITIVE_ORDER);
        List<String> s = new ArrayList<>();
        // 使用指定构比较器排序
        s.sort(comparator);
    }
}
