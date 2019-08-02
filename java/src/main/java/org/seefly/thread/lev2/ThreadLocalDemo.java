package org.seefly.thread.lev2;

import org.junit.Test;
import org.seefly.thread.ThreadUtils;

import java.lang.ref.WeakReference;

/**
 * 误区
 * 1、一个ThreadLocal并不是一个Map结构，不是每个线程做key，然后只能放一个值
 *      一个ThreadLocal就是一个"key"，而这个"Map"结构是放在每个线程里面去保存的
 *
 *  Q：既然ThreadLocal相当于一个key，但是为什么能够通过ThreadLocal拿到每个线程中的私有的值呢？
 *  A：因为ThreadLocal里面的get方法其实是访问的线程中的"Map",然后以自己(this)为key去取值。
 *
 *  Q：那是不是能够通过不同的ThreadLocal在一个线程中存放多个值呢？
 *  A：是的
 *
 *  Q：同一个ThreadLocal用在多个线程中有没有问题？
 *  A：有个毛的问题，虽然"key"一样，但是"map"又不是一个
 *
 *  Q：是不是能够通过ThreadLocal解决线程安全问题？
 *  A：狗屁，所谓线程安全就是多线程对共享数据的并发读写。这东西都线程隔离了，肯定不是用来
 *  进行线程间数据共享的。也就谈不上线程安全。
 *
 * ThreadLocal结构
 * ThreadLocal中的静态内部类ThreadLocalMap相当于数据存储结构
 *  它在Thread中被当作成员变量，但并不会随着线程的初始化而初始化，他会在
 *  当前线程第一次调用ThreadLocal.get/set方法的时候进行初始化。
 *
 * 内存泄漏
 * 由于线程池技术的引入，一个线程在使用过后并不会立马销毁，会放到线程池中重复使用。
 * 如果使用了ThreadLocal而没有及时释放资源，那么就会造成内存泄漏。
 *
 * 为了减少内存泄漏的可能性 ThreadLocalMap.Entry extends WeakReference<ThreadLocal<?>>
 *     键值对中的key被设计为弱引用的，所以若外部没有实例对ThreadLocal强引用的话，则会被回收。
 *     此时键值对就会成为 Null:Value 这样，值还是强引用没有被回收。但 在ThreadLocal中get/set/remove方法都会将key为null的从Thread的“map”中移除.
 * 但这也不能保证傻逼弄出内存泄漏。
 * @author liujianxin
 * @date 2019-08-02 09:48
 */
public class ThreadLocalDemo {

    private WeakReference<Object> o = new WeakReference<>(new Object());


    @Test
    public void testWeak(){
        System.out.println("开课拉！");
        System.gc();
        System.gc();
        System.out.println(o.get());
    }



    /**
     * 只要保证多线程中每个线程拿到的都是同一个TA/TB，而不是新建的
     */
    private static final ThreadLocal<String> TA = new ThreadLocal<>();
    private static final ThreadLocal<Integer> TB = new ThreadLocal<>();



    /**
     * 基本功能演示
     * 子线程和主线程ThreadLocal隔离数据
     */
    @Test
    public void testA(){
        Thread thread = new Thread(() ->{
            TA.set("sav");
            String s = TA.get();
            ThreadUtils.sleep(20);
            System.out.println(s);
            TA.remove();
        });
        thread.start();
        ThreadUtils.sleep(10);
        System.out.println(TA.get());
    }

    /**
     * 一个ThreadLocal相当于一个key
     * “Map”放在线程中
     */
    @Test
    public void testTwo(){
        Thread thread = new Thread(() ->{
            TA.set("this is ThreadLocal A");
            TB.set(2);
            ThreadUtils.sleep(200);
        });
        thread.start();
        ThreadUtils.sleep(20);
        System.out.println(TA.get());
        System.out.println(TB.get());
    }


    /**
     * 学习API
     *
     * 使用静态方法，这样在第一次get的时候可以获取这个初始值
     * 注意并不是立即在当前线程设置这个ThreadLocal到线程中的
     *
     * 真没啥东西，源码都这么简单
     */
    static ThreadLocal<String> api = ThreadLocal.withInitial(() ->"OK");
    @Test
    public void learnAPI(){
        String s = TA.get();
        TA.remove();
    }
}
