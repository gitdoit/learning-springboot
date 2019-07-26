package org.seefly.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Arraylist 与 LinkedList 区别?
 * 1. 是否保证线程安全：
 *      ArrayList 和 LinkedList 都是不同步的，也就是不保证线程安全；
 *
 * 2. 底层数据结构：
 *      Arraylist 底层使用的是 Object 数组；LinkedList 底层使用的是 双向链表 数据结构
 *
 * 3. 插入和删除是否受元素位置的影响：
 *      1、ArrayList 采用数组存储，所以插入和删除元素的时间复杂度受元素位置的影响。
 *      比如：执行add(E e) 方法的时候， ArrayList 会默认在将指定的元素追加到此列表的末尾，这种情况时间复杂度就是O(1)。
 *      但是如果要在指定位置 i 插入和删除元素的话（add(int index, E element) ）时间复杂度就为 O(n-i)。
 *      因为在进行上述操作的时候集合中第 i 和第 i 个元素之后的(n-i)个元素都要执行向后位/向前移一位的操作。
 *
 *      2、LinkedList 采用链表存储，所以插入，删除元素时间复杂度不受元素位置的影响，都是近似 O（1）而数组为近似 O（n）。
 *
 * 4. 是否支持快速随机访问：
 *      LinkedList 不支持高效的随机元素访问，而 ArrayList 支持。
 *      快速随机访问就是通过元素的序号快速获取元素对象(对应于get(int index) 方法)。
 *
 * 5. 内存空间占用：
 *      ArrayList的空 间浪费主要体现在在list列表的结尾会预留一定的容量空间，
 *      而LinkedList的空间花费则体现在它的每一个元素都需要消耗比ArrayList更多的空间（因为要存放直接后继和直接前驱以及数据）。
 *
 * @author liujianxin
 * @date 2019-07-25 14:52
 */
public class ListDemo {

    @Test
    public void testList(){
        /**
         * 无参构造方式，默认使用空数组作为容器 -> private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
         */
        ArrayList<String> defaultList = new ArrayList<>();
        defaultList.add("");

        /**
         * 指定容量的构造方式，会创建一个指定长度的数组
         * new Object[initialCapacity]
         */
        ArrayList<String> capacityList = new ArrayList<>(16);

        /**
         * 使用集合作为构造参数
         */
        ArrayList<String> copyList = new ArrayList<>(Arrays.asList("",""));

    }


    /**
     * ArrayList扩容机制
     */
    @Test
    public void testListCapacity(){
        // 无参构造方法会指定一个空数组，所以，只有在第一次调用add方法的时候才会对
        // 列表进行扩容以及指定默认容量
        ArrayList<String> defaultList = new ArrayList<>();

        /**
         * 当添加第一个元素的时候，由于无参构造方法使用的是空数组作为容器
         * 所以这一步需要将空数组进行扩容，使用默认容量10来构造一个容量为10的数组
         * 1、每次add元素之前调用ensureCapacityInternal()方法检查数组大小是否能够容纳新元素
         * 2、如果数组容量不够，则会调用grow()方法进行扩容
         * 3、由于初始的空数组不能容纳，则会扩容起始容量10
         */
        defaultList.add("");
        defaultList.add("");
        defaultList.add("");
        defaultList.add("");
        defaultList.add("");
        defaultList.add("");
        defaultList.add("");
        defaultList.add("");
        defaultList.add("");
        defaultList.add("");
        /**
         * 当添加第11个元素的时候，容量为10的数组不够用了
         * 这时候的策略就是创建一个原数组1.5倍容量的新数组
         * 并将原数组中的元素拷贝到新数组中
         *
         *  elementData = Arrays.copyOf(elementData, 15)
         */
        defaultList.add("");
    }


    @Test
    public void testArrayCopy(){
        int[] a = new int[10];
        a[0] = 0;
        a[1] = 1;
        a[2] = 2;
        a[3] = 3;

        //arg1 原数组
        //arg2 起始位置索引(包括)
        //arg3 目标位置
        //arg4 目标位置索引(包括)
        //arg5 从原数组复制的元素数量
        //so，这个就是从数组索引为2的开始，复制一个元素，到目标素组中索引为4的位置
        //效果就是，将2号索引元素复制一份到3号索引后面
        //output->0 1 2 3 2 0 0 0 0 0 0
        System.arraycopy(a,2,a,4,1);

        for (int i : a) {
            System.out.println(i);
        }

    }

    /**
     * Arrays.copyOf()底层调用的也是System.arraycopy
     * 这个方法的目的就是为了方便的给数组扩容
     */
    @Test
    public void testCopyOf(){
        int[] old = {1,2,3};
        // 寄居蟹一样，原来的家太小了，给放个大的
        int[] newOne = Arrays.copyOf(old, 6);
        System.out.println(newOne.length);
    }


    /**
     * list.toArray方法很有意思
     * 如果传入的参数数组容量足够大，那么将原列表中的元素复制一份到这个参数数组中
     * 如果不够大，那么就根据参数列表类型创建一个新的数组，当作返回值返回回去。
     *
     * 所以一般都用list.toArray(new Integer[0]);然后取其返回值
     */
    @Test
    public void testToArray(){
        List<Integer> list = Arrays.asList(1, 2, 3);


        Integer[] integers = list.toArray(new Integer[0]);
        for (Integer integer : integers) {
            System.out.println(integer);
        }

        Integer[] b = new Integer[3];
        Integer[] c = list.toArray(b);
        for (Integer integer : b) {
            System.out.println(integer);
        }

        for (Integer integer : c) {
            System.out.println(integer);

        }

    }


    /**
     *  arrayList.ensureCapacity(n);
     *  这方法是给用户用的，目的就是让用户在添加大量数据前
     *  能够手动扩容列表容量
     *  避免列表自己一次次的扩容，提升效率。
     */
    @Test
    public void testEnsureCapacity(){
        ArrayList<Object> list = new ArrayList<Object>();
        final int N = 10000000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            list.add(i);
        }
        long endTime = System.currentTimeMillis();
        // 使用ensureCapacity方法前：2555
        System.out.println("使用ensureCapacity方法前："+(endTime - startTime));

        list = new ArrayList<Object>();
        long startTime1 = System.currentTimeMillis();
        list.ensureCapacity(N);
        for (int i = 0; i < N; i++) {
            list.add(i);
        }
        long endTime1 = System.currentTimeMillis();
        // 使用ensureCapacity方法后：751
        System.out.println("使用ensureCapacity方法后："+(endTime1 - startTime1));

    }

}
