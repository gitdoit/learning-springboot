package org.seefly.lambda.function;

import java.util.HashSet;
import java.util.Set;
import java.util.function.*;

/**
 * java8在2014年发布，今年是2018年。然而java8的这些新特性都还没掌握全....
 *
 * 演示java8的函数式编程，{@link java.util.function}包下默认提供了一些函数式接口，演示一下。
 *
 * 1、函数型，有输入，有输出。{@link Function}
 * 2、消费型，有输入，无输出，一般情况下需要产生副作用。{@link Consumer}
 * 3、判断型，有输入，返回布尔值。{@link Predicate}
 * 4、供给型，无输入，有输出。{@link Supplier}
 *
 * 这四种类型下默认提供了一些针对性的操作。不再一一演示。
 * @author liujianxin
 * @date 2018-11-22 19:40
 */
public class FunctionDemo {
    public static void main(String[] args){
        Set<Integer> set = new HashSet<>();

        // 一个输入一个输出的
        Function<Integer,Integer> adder = a -> a + 9;
        adder.compose(a -> a.hashCode()+ 9).andThen(Object::hashCode);
        //两个输入一个输出的
        BiFunction<Integer,Integer,Integer> biFunction = (a,b)-> a+b;

        //一个输入没有输出的,需要通过副作用(指修改了函数外部的状态)进行操作
        Consumer<Integer> consumer = set::add;
        //两个输入没有输出的，需要通过副作用进行操作
        BiConsumer<Integer,Integer> biConsumer = (a,b) -> {set.add(a);set.add(b);};

        //一个输入，返回true false
        Predicate<String> predicate = str -> str.length() > 5;
        predicate.and(str -> str.length() < 10);

        //两个输入，返回true false
        BiPredicate<Integer,Integer> biPredicate = (a,b) -> a + b < 10;
        biPredicate.and((a,b) -> a - b < 3);

        // 无输入，有输入
        Supplier<Long> supplier = System::currentTimeMillis;

    }


}
