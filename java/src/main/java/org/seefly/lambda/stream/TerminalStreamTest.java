package org.seefly.lambda.stream;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author liujianxin
 * @date 2018-07-09 15:43
 * 描述信息：Terminal是一个消耗流操作
 **/
public class TerminalStreamTest {

    /**
     * 参考：https://blog.csdn.net/icarusliu/article/details/79504602
     *
     * reduce的作用是将stream元素组合起来
     * 这样说好像有些不对劲，这个操作会将stream中所有的元素都参与到计算中
     * 然后返回一个结果。
     * 你可以将这些元素组合起来然后返回一个结果，也可以将这些元素求一下平均值，最大值，等等
     *
     *
     * 一个参数的Reduce操作，操作过程是
     * 传入参数(a[0],a[1]) 自定义操作返回结果 c(返回值类型要和参数类型一样)
     * 这个返回值会和下一个元素重复上述操作
     */
    @Test
    public void testReduce(){
        //组合
        Stream<String> stream = Stream.of("a", "b", "c", "d");
        System.out.println( stream.reduce("", String::concat));
        //System.out.println(stream.reduce("",(a,b) -> a+b));

        //求最大值
        Stream<Integer> stream1 = Stream.of(1, 2, 3, 4);
        //stream1.reduce(Integer::max);
        Optional<Integer> reduce = stream1.reduce((a, b) -> a > b ? a : b);
        reduce.ifPresent(System.out::print);


        //求和
        Stream<Integer> stream2 = Stream.of(1, 2, 3, 4);
        //stream2.reduce((a,b) ->a+b);
        Optional<Integer> reduce1 = stream2.reduce(Integer::sum);
        reduce1.ifPresent(System.out::print);


        //两个参数的Reduce基本上和一个参数的类似，只不过第一个参数.
        //用来做初始值，而且返回值也不是一个Optional了，因为不论怎么样也会有一个初始值
        Stream<Integer> stream3 = Stream.of(1, 2, 3, 4);
        System.out.println(stream3.reduce(3, Integer::sum));
    }

    /**
     * 三个参数的Reduce
     * 跟前两个用法有很大区别
     * 第一个参数R，就是返回值类型，它没有象前两个操作那样对返回值做了限定
     * 这个U可以是任何类型，所以这样用法就多了起来，我们可以对一个字符数组进行求所有字符串长度的和，然后返回这个长度值
     *
     * 第二个参数 R BiFunction(R,T)，它在这里规定了返回值要和上面的R一样，且它的第一个参数类型也为R，T为Stream流元素类型
     *
     * 第三个参数是用来做并行操作用的
     */
    @Test
    public void testReduce3(){

        /*****************非并行操作***********************/
        // 将Stream转List
        Stream<String> stream = Stream.of("a", "b", "c", "d");
        ArrayList<String> reduce = stream.reduce(new ArrayList<>(), (r, t) -> {
            r.add(t);
            return r;
        }, (r1, r2) -> r1);
        System.out.println(reduce);

        // 求字符长度值和
        Stream<String> stream1 = Stream.of("abc", "def");
        Integer reduce1 = stream1.reduce(new Integer(0), (r, t) -> {
            r += t.length();
            return r;
        }, (r1, r2) -> r1);
        System.out.println(reduce1);

        // 串行操作，对列表中的数字累计求和。最后一个参数在并行操作的时候才起作用
        Stream<Integer> stream3 = Stream.of(1, 2, 3, 4);
        Integer reduce2 = stream3.reduce(new Integer(0), Integer::sum, (s1,s2) ->s1);
        System.out.println(reduce2);


        /*******************并行操作****************************/

        // 并行操作，列表中的每个元素都分别和初始元素进行求和操作
        // 然后将他们的和，再通过第三个参数进行求和操作
        // 就是-> (0+1) * (0+2) * (0+3) * (0+4)
        // 注意，这里的BigDecimal有个坑，就是调用它的加减乘除等操作的时候不会再原来的对象上进行更改，而是操作之后返回一个新的对象，原有对象不改变
        Stream<Integer> stream2 = Stream.of(1, 2, 3, 4);
        BigDecimal reduce3 = stream2.parallel().reduce(new BigDecimal(0), (r, t) -> r.add(new BigDecimal(t)), BigDecimal::multiply);
        System.out.println(reduce3);
    }

}
