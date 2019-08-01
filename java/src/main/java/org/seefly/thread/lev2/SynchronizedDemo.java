package org.seefly.thread.lev2;

/**
 * synchronized
 * 有三种使用方式
 * 1、修饰实例方法
 *  如果修饰实例方法，在进入同步代码前需要获取当前实例对象的锁
 * 2、修饰静态方法
 *  如果修饰静态方法，在进入同步代码前需要获取当前类的class对象的锁
 * 3、修饰代码块
 *  如果修饰代码块，需要手动指定一个对象做锁
 *
 *  谈谈 synchronized和ReentrantLock 的区别
 *  1、两种都是可重入锁
 *  2、synchronized依靠jvm，而ReentrantLock依赖API，也就是依靠jdk来实现的。
 *  3、ReentrantLock更加高级，用法更为丰富。并且可以实现公平锁，可以等待中断等，而
 *      synchronized则是一个非公平锁。
 *  说说 synchronized 关键字和 volatile 关键字的区别
 *  1、volatile只能修饰变量
 *  2、多线程中访问volatile变量不会发生阻塞
 *  3、volatile可以保证数据的可见性，不能保证数据的原子性。synchronized两者都可以保证
 *  4、volatile用多线程间的数据可见性，而synchronized用于共享资源的互斥访问。
 *
 *  ===========================================================
 *  volatile
 *  字面意思是易变的，不稳定的。
 *  它会通知线程这个变量是不稳定的，如果需要读，则不能从本地缓存中读取，需要从主存中读取
 *  对于写线程则在更新之后需要立马刷新到主存中。
 *
 *
 *
 * @author liujianxin
 * @date 2019-08-01 14:59
 */
public class SynchronizedDemo {

    /**
     * 使用volatile保证可见性
     * Q：已经使用同步关键字保证一个实例了，为什么还要用volatile？作用是什么？
     * A：single = new SynchronizedDemo();并不是一个原子操作
     *  它分为三个步骤
     *  1、分配空间
     *  2、创建实例
     *  3、指向该实例
     *  但是可能会发生指令重排序
     *  实际情况可能就是1 3 2，所以在判断 single == null 的时候返回false
     *  直接把一个还没初始化完成的对象返回出去了，这就出问题了。
     *  而volatile则会避免指令重排序
     */
    private static volatile SynchronizedDemo single;

    private SynchronizedDemo(){}

    /**
     * Q:为啥不在方法上直接用同步关键字呢？
     * A:这样的话所有的线程访问这个方法都需要加锁了，明显是不需要的，只有在第一次创建的时候才需要同步控制。
     *
     * Q：为啥要两次判断呢？
     * A：多线程环境下A线程判断single == null成立，然后被切换掉。B线程判断也成立，然后实例化一个。
     *    A线程来了之后也会创建一个，如果两次判断就没这个问题了。
     *
     *
     */
    public static SynchronizedDemo getInstance(){

        if(single == null){
            synchronized (SynchronizedDemo.class){
                if(single == null){
                    return   single = new SynchronizedDemo();
                }
            }
        }
        return single;
    }


}
