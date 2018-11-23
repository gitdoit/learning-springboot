package org.seefly.lambda.stream;

import org.junit.Test;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * @author liujianxin
 * @date 2018-07-04 22:20
 * 描述信息：
 **/
public class StartStream {


    @Test
    public void test1(){
        List<String> list = new ArrayList<>();
        list.add("a");
        //过滤空集合，否则当后面的操作会报错。这里即使过滤掉了所有的元素 后面的操作都不会报异常
        Stream.of(list, null).filter(CollectionUtils::isNotEmpty).flatMap(List::stream).forEach(System.out::print);
    }
    /**
     * 创建Stream流的几种方式
     */
    @Test
    public void testBuildStream(){

        //注意看下面这三种用法的不同情况
        //根据of方法的说明，他会返回一个以参数类型为泛型的Stream
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4);
        //但是这里参数类型明明是String[] 返回值类型却不会以String[]来做泛型，还是用String
        Stream<String> type1 = Stream.of(new String[]{"a", "b", "c"});
        //这里就是用参数类型做泛型了
        Stream<ArrayList<Object>> arrayListStream = Stream.of(new ArrayList<>());



        //上面的Strem.of里面调用的还是Arrays.Stream
        //对于基本类型数值（IntStream、LongStream、DoubleStream）有对应的Stream流，但是也可以使用泛型的方式，但是这样会涉及到拆箱装箱动作，会耗费资源
        Stream<Integer> type2 = Arrays.stream(new Integer[]{1, 2, 3, 4});
        IntStream intStream = IntStream.of(1, 2, 3, 4);

        Stream<String> type3 = new ArrayList<String>().stream();
    }


    /**
     * 转换流
     * 一个流只能使用一次，下面的会报错
     */
    @Test
    public void testTransferStream(){
        //流转换为数组
        Stream<String> stream1 = Stream.of(new String[]{"a", "b", "c"});
        String[] arr = stream1.toArray(String[]::new);
        //流转换为list
        Stream<String> stream2 = new ArrayList<String>().stream();
        List<String> list = stream2.collect(Collectors.toList());
        //这里一个HahsSet::new 还有其他的等等
        ArrayList<String> list2 = stream2.collect(Collectors.toCollection(ArrayList::new));
        //流转换为字符串
        String str = stream2.collect(Collectors.joining(""));
    }

    /**
     * 看看Collectors.joining是干啥的
     * 发现他就是将list转为String并以指定字符分割
     * 跟lang3包里面的StringUtils.join一个样。
     * 都可以分割各种基本类型的list
     */
    @Test
    public void testJoin(){
        //Stream<String> stream1 = Stream.of(new String[]{"a", "b", "c"});
        List<String> list = new ArrayList<>(Arrays.asList(new String[]{"s","v"}));
        String collect = list.stream().collect(Collectors.joining(","));
        System.out.println(collect);
    }



}
