package org.seefly.collection;

import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HashMap Vs Hashtable
 * 1、线程安全
 *  HashMap是非线程安全的，Hashtable是线程安全的；
 *  Hashtable内部方法基本都经过synchronized关键字修饰
 * 2、效率
 *  很明显为了保证线程安全Hashtable的效率是不如HashMap的
 * 3、Null key/value
 *  HashMap支持一个Null键多个Null值
 *  Hashtable不支持任何Null键Null值
 * 4、初始容量和每次扩容量
 *  Hashtable默认的初始容量为11，每次扩容，容量增加2n+1
 *  HashMap默认容量16，每次扩容容量增加2倍，也就是说它的容量始终为2的n次幂(But why?)
 * 5、底层数据结构
 *  JDK8之后的HashMap在Hash冲突后如果链表长度大于8时会将链表转为红黑树。
 *
 *  HashMap的数据结构
 *  使用的是数组+链表/红黑树的方式
 *  1、在put键值对的时候，首先拿到key的hash，然后经过内部的扰动函数(对原hash进行扰动，为了减少hash冲突；(key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);)
 *  2、通过计算后的hash值和数组长度取余就能获取到这个key在数组中的位置
 *      map.put方法中的源码
 *      if ((p = tab[i = (n - 1) & hash]) == null)
 *             tab[i] = newNode(hash, key, value, null);
 *      可以看到(n - 1) & hash，由于数组长度n总是2的n次幂，所以它也就相当于 i = hash%n。由于&运算效率高于%运算，所以讲map容量设计为2的n次幂
 *  3、如果hash碰撞了(通过比较hash值(扰动后的)是否相等(用的 ==))
 *      1、(keyOld == keyNew || keyOld.equals(keyOld))若成立，则覆盖
 *      2、否则若当前位置的节点是个红黑树，则直接追加进去
 *      3、否则若当前节点不是红黑树，则放后面，若链表长度超过默认阈值8，就转变为红黑树
 *
 *
 *
 * @author liujianxin
 * @date 2019-07-26 10:14
 */
public class MapDemo {

    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 构造方法
     *          // ... 省略对初始化容量的校验
     *          // 负载因子默认 0.75f
     *         this.loadFactor = loadFactor;
     *         // 确定阈值，将构造参数中的初始化容量转换为2的n次幂
     *         this.threshold = tableSizeFor(initialCapacity);
     *
     * */
    @Test
    public void test(){
        HashMap map = new HashMap(16,  0.77f);
        map.put("sdf","sdf");
    }





    /**
     * 保证HashMap总是使用2的n次幂大小作为其容量
     * 即使你在构造函数中传入13 也会通过这个方法变为16
     */
    @Test
    public  void tableSizeFor() {
        // >>> 无符号 右移
        // |= 按位或然后赋值
        int n = 13 - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        int a  = (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
        System.out.println(a);
    }


    /**
     * 由于HashMap并不是线程安全的
     * 所以当多线程同时操作一个HashMap时，所造成的不仅仅是数据紊乱
     * 还有可能造成死循环！！从而导致Cpu 100%
     * 这是由于两个线程同时对一个HashMap进行reHash时，同一个tab[i]下的链表首尾相连形成了一个圈造成的。
     * https://coolshell.cn/articles/9606.html
     */
    @Test
    public void testCurrentLoop(){

    }


    /**
     * java1.7及以前ConcurrentHashMap使用的是分段数组+链表的形式
     * 就相当于每一段的数组都是用不同的锁，这样保证了效率，不至于对全表加锁。
     * 而java1.8之后，就摈弃了这种分段数组的方式，改成和HashMap一样，数组+链表
     * 并发控制使用同步关键字
     */
    public void testConcurrentHashMap(){
        ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();
        map.put("","");
    }



}
