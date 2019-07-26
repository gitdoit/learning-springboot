package org.seefly.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.*;

/**
 * 排序接口{@link Comparable}和排序器
 * @author liujianxin
 * @date 2019-07-26 14:04
 */
public class SortDemo{

    @Test
    public void testCompara(){
        List<Integer> integers = Arrays.asList(1,2,3);
        // 对列表进行自然排序，它要求列表中的元素必须实现Comparable接口
        // 自然升序
        Collections.sort(integers);
        // 等同于这个
        integers.sort(null);


        // 排序器，如果自然排序无法满足，可以定制一个排序器
        // 例如想对Student列表进行按年龄排序，另外还想按照身高排序
        // 这时候由于只能实现一个Comparable接口，所以必须使用排序器来实现
        Comparator<String> comparator = ( a, b) ->{return b.compareTo(a);};
    }



    @Test
    public void testComparable(){
        List<Student> students = Arrays.asList(new Student(11, 178.5d), new Student(12, 166.6),new Student(5,55));
        // 对实现了Comparable接口的Student进行自然升序排列
        students.sort(null);
        for (Student student : students) {
            System.out.println(student);
        }

        // 对列表进行反转，如果反转之前是无序的，排列之后也是无序的。
        //Collections.reverse(students);


        for (Student student : students) {
            System.out.println(student);
        }

    }

    /**
     * 使用排序器
     */
    public void testComparator(){
        List<Student> students = Arrays.asList(new Student(11, 178.5d), new Student(12, 166.6),new Student(5,55));
        // 对列表进行反序排列，Student的自然排序是按照年龄升序，现在就是按照年龄倒序了
        students.sort(Comparator.reverseOrder());

        // 对列表进行按照身高升序排列
        Comparator<Student> comparator = (o1, o2) -> (int) (o1.getHeight() - o2.getHeight());
        students.sort(comparator);
        Collections.sort(students,comparator);

        // 这就不行了，这个用于数组
        //Arrays.sort();
    }









    @Data
    @AllArgsConstructor
    public static class Student implements Comparable<Student>{
        private int age;
        private double height;

        /**
         * 将此对象与指定的order对象进行比较。
         * 返回一个负整数、零或正整数，因为该对象小于、等于或大于指定的对象。
         * ->
         *      如果当前对象大于传入的对象将返回正整数
         *      等于返回0
         *      小于返回负整数
         *
         * 对于所有x和y，实现者必须确保sgn(x. compareto (y)) == -sgn(y. compareto (x))
         * (这意味着x. compareto (y)必须抛出异常，如果y. compareto (x)抛出异常。)
         * ->   也就是说要保证对称性
         *
         *
         * 实现者还必须确保关系是可传递的:(x.compareTo(y)> && y. compareto (z)>0)表示x.compareTo(z)>0
         * ->   也就说要保证传递性
         *
         * 最后，实现者必须确保x.compareTo(y)==0表示sgn(x.compareTo(z)) == sgn(y. compareto (z))，对于所有z。
         * ->   保证一致性
         *
         * 一般来说，任何实现可比接口并违反此条件的类都应该清楚地指出这一事实。
         * 推荐的语言是“注意:该类的自然顺序与equals不一致。”
         * 在前面的描述中，符号sgn(表达式)指定了数学sgn函数，根据表达式的值是负、零还是正，sgn函数被定义为返回- 1,0或1中的一个。
         *
         *
         */
        @Override
        public int compareTo(Student o) {
            return age - o.getAge();
        }
    }

}
