package org.seefly.juc;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 这不就是乐观锁吗？ ->)
 * 只对引用保证原子性
 * https://stackoverflow.com/questions/3964211/when-to-use-atomicreference-in-java
 * @author liujianxin
 * @date 2018-11-19 18:11
 */
public class AtomicReferenceDemo {
    public static void main(String[] args){
        // 临界区资源
        AtomicReference<Object> cache = new AtomicReference<>(new Object());
        Object cachedValue = cache.get();
        Object cachedValueToUpdate = cache.get();
        // 做一些任务，期间临界区资源可能被修改
        Object newValue = someFunctionOfOld(cachedValueToUpdate);
        // 被改变了则操作失败
        boolean success = cache.compareAndSet(cachedValue,cachedValueToUpdate);
    }

    private static Object someFunctionOfOld(Object param){
        //... do some work to transform cachedValueToUpdate into a new version
        return param;
    }
}
