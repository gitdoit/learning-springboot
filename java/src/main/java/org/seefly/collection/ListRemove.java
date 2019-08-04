package org.seefly.collection;

import org.junit.Test;

import java.util.*;

/**
 * 在遍历元素中移除元素
 * 1、使用普通for循环来遍历列表，条件成立时移除指定元素，然后游标-1
 * 2、使用迭代器迭代列表，条件成立时调用迭代器的remove方法移除元素。
 * 3、使用增强for循环迭代元素(编译后还是用的迭代器)，条件成立时调用list.remove(target).之后要立马跳出
 *    不然会抛出并发修改异常
 * 4、超级牛逼的{@link ArrayList#removeIf(java.util.function.Predicate)}
 *    看了看源码，就这个好使，用位图标记需要删除的元素下标，然后根据位图来把需要删除的元素覆盖掉就OK。
 *    ArrayList.remove(.)方法每调用成功一次就要System.arrayCopy一下。这多费劲啊
 *    不过不是在遍历的时候调用的话还是可以的，毕竟方法4还是需要遍历整个列表的而且时间复杂度至少是O(n+1)
 *
 * 关于遍历列表中的并发修改异常
 *  ArrayList中维护了一个modCount变量用来记录列表被修改的次数，而它内部类的迭代器实现
 *  里面也有一个功能类似的变量，每次调用next方法前都会校验这两个数值是否相同，不同就会抛出这个异常。
 *
 * @author liujianxin
 * @date 2019-08-04 16:40
 */
public class ListRemove {

    /**
     * 首先看一下删除方法
     * remove方法调用的System.arrayCopy
     * 也就是说把指定索引位置的元素后面的都向前移动一位
     * 此时最后两个元素是一样的，然后把最后一位元素置为null就OK了
     */
    @Test
    public void testRemove(){
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5,5,6));
        ListIterator<Integer> integerListIterator = list.listIterator();
        list.remove(1);
    }


    /**
     * 如果需要删除列表中所有符合条件的元素的话，用这个最合适
     * 但是例如removeFirst这种逻辑的话，还是用list.remove()好点
     */
    @Test
    public void testRemoveIf(){
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5,5,6));
        // 删除列表中所有值等于5的元素
        list.removeIf(item -> Objects.equals(item,5));
    }

    /**
     * 普通for循环删除元素
     * 遍历中删除元素需要注意由于调用的remove方法会
     * 使集合中元素向前移动，同时游标又再自增，那么此时会跳过指定删除元素后的那一位。
     */
    @Test
    public void testForRemove(){
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5,5,6));
        for (int i = 0; i < list.size(); i++) {
            if(Objects.equals(list.get(i),5)){
                // 由于元素的移动，游标需要自减一位
                list.remove(i--);
            }
        }
        System.out.println(list);
    }

    /**
     * 使用迭代器删除元素
     * 迭代器用的是java.util.ArrayList.Itr
     * 对于迭代器删除元素 iterator.remove() --call--> ArrayList.remove(index)
     */
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5,5,6));
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()){
            if(Objects.equals(iterator.next(),5)){
                iterator.remove();

                // 这样玩的话会使modCount+1
                // 然后调用iterator.next()方法时会比较modCount和迭代器自己维护的一个类似的遍历
                // 不同的话就抛异常
                // list.remove(4);
            }
        }
        System.out.println(list);
    }

    /**
     * 增强for循环编译之后其实就是调用的迭代器
     * 咋办呢？我们知道ArrayList实现了随机访问接口
     * 表明这个集合支持快速的随机访问，也就是说通过下标访问元素比迭代器快
     * 所以还是用普通for循环来的好
     */
    @Test
    public void testForEachRemove(){
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5,5,6));
        for(Integer integer : list){
            if(Objects.equals(integer,5)){
                // 在用迭代器迭代元素的同时
                // 调用这个方法之后再调用iterator.next()会抛出异常
                // 而增强for循环编译之后就是用的迭代器，所以这样会出问题
                list.remove(integer);
                // 但是咱可以再删除元素之后跳出循环不让他调用next方法就行了
                // 不过这种做法解决不了问题
                break;
            }
        }
    }
}
