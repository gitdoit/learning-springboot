package org.seefly.juc;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 简介：
 *  队列、无界、先进先出、线程安全、元素不能为null、无锁(采用了很牛逼的算法保证线程安全)、高效
 *
 * @author liujianxin
 * @date 2018-09-26 16:31
 */
public class MyConcurrentLinkedQueue {

    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    }
}
