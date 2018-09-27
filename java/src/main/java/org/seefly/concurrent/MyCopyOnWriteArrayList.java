package org.seefly.concurrent;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 简介
 *  线程安全、读操作无锁、写操作全复制并替换
 *  对于读操作远远高于写操作的并发环境，可以考虑使用这个类来作为共享变量
 *  由于读操作不会修改原有数据，因此对读操作的加锁是一种资源浪费。所以在该类的实现中
 *  读操作是不需要加锁的，那么怎么控制数据不一致呢？
 *  这里的实现方式是在写操作的时候进行一次自我复制，对复制的内容进行修改，再将复制的替换原本的即可。
 *  这个跟数据库事务很像啊，事务开起来，没提交之前别的都读不到
 * @author liujianxin
 * @date 2018-09-26 18:19
 */
public class MyCopyOnWriteArrayList {
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> cow = new CopyOnWriteArrayList<>();
    }
}
