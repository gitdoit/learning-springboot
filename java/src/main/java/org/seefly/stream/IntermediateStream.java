package org.seefly.stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

/**
 * @author liujianxin
 * @date 2018-07-04 23:12
 * 描述信息：Intermediate类型的流操作可以在使用方法之后再返回一个流
 * 对这个流进而进行其他后继操作
 **/
public class IntermediateStream {

    /**
     * map方法就是一对一映射，遍历每个元素执行给定的操作映射成另一个目标结果，然后再返回一个stream进行后续的操作
     */
    @Test
    public void testMap(){
        //字符拼接
        Stream<String> stream = new ArrayList<>(asList("a","b","c")).stream();
        String str = stream.map(a -> a + "s").collect(joining());
        System.out.println(str);
        //小写转大写
        Stream<String> stream1 = new ArrayList<>(asList("a","b","c")).stream();
        String str2 = stream1.map(String::toUpperCase).collect(joining());
        System.out.println(str2);
        //数学绝对值。只要参数对的上，什么静态方法都可以这样调用
        Stream<Integer> stream2 = new ArrayList<>(asList(-1, 2, -3, 4)).stream();
        stream2.map(Math::abs).forEach(System.out::print);
    }

    /**
     * flatMap直译过来就是 扁平化映射，就是合并多个流成为一个流
     * 他需要传的Function中限定返回值只能时一个流，而map中的Function没有做限定
     * 既然map和flatMap的参数都是Function，区别只是一个限定返回值一个没有
     * 那么能不能将map代替flatMap呢，答案是不行的，因为即使给map传一个返回值是stream的Function
     * 这个map方法的返回值确是一个Stream<Stream<?>>,而给flatMap传相同的参数，他的返回值是一个Stream<?>
     *
     * 所以FlatMap常用来合并流，例如将多个集合或者list合并为一个流，然后再做后续流操作
     */
    @Test
    public void testFlatMap(){
        Stream<List<Integer>> listStream = Stream.of(asList(1, 2, 3, 4), asList(1, 2, 3, 4));
        Stream<Integer> stream = new ArrayList<>(Arrays.asList(1, 2, 3, 4)).stream();
        Stream<ArrayList<Integer>> arrayListStream = Stream.of(new ArrayList<>(Arrays.asList(1, 2, 3, 4)));

        //Stream<Stream<List<Integer>>> streamStream = listStream.map(Stream::of);
        // 可见多个list流合并为一个list流
        Stream<Integer> integerStream = listStream.flatMap(s -> s.stream());
        //listStream1.forEach(System.out::print);
    }


    /**
     * 测试过滤流，当返回值为真时 元素被保留 为假时元素被过滤
     */
    @Test
    public void testFilter(){
        Stream<String> stream = new ArrayList<>(asList("a","b","c")).stream();
        //瞅瞅这个，还能直接调用静态方法连类名都不需要了直接 joining()
        System.out.println(stream.filter(StringUtils::isNotBlank).collect(joining()));

        Stream<Integer> stream1 = new ArrayList<>(asList(1, 2, 3, 4)).stream();
        stream1.filter(i -> i%2 == 0).forEach(System.out::print);
    }


    /**
     * 测试peek操作，直译过来就是瞥一眼，API上介绍说，这个通常用来debug用的
     * 这个操作跟forEach很象，但是forEach是一个消耗流操作，这个不是
     * 他就是瞥一眼，不能对元素有任何操作.
     * 注意：如果一个流，在没有被消耗掉之前，使用peek他是不会起作用的。
     * 也就是说，你想使用peek瞥一眼，只有在流被消耗掉的时候它才去瞥一眼，不然不会
     */
    @Test
    public void testPeek(){
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
        // 看看这个操作，先是过滤奇数，然后瞥一眼
        // 再将过滤后的之加一，再瞥一眼。本以为打印的结果应该是 2 4 3 5.但是没有想到是2345
        // 为啥呢？我也不知道
        stream.filter(e -> e % 2 == 0).peek(System.out::print).
               map(e -> e + 1).peek(System.out::print).
               collect(Collectors.toList());

        Stream.of("one", "two", "three", "four") .filter(e -> e.length() > 3)
               .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());
    }

    /**
     * findFirst和findAny
     * findFist是找到第一个
     * findAny是找到任意一个
     * 示例：https://zhidao.baidu.com/question/1498838916214323339.html
     * 下面的几种方法都会返回一个Optional
     */
    @Test
    public void testFindFirst(){
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
        stream.max(Integer::compareTo).ifPresent(System.out::print);
        stream.min(Integer::compareTo).ifPresent(System.out::print);
        stream.findFirst().ifPresent(System.out::print);
        stream.findAny().ifPresent(System.out::print);
    }




}
