package org.seefly.cache.redisops;

import org.junit.Test;
import org.springframework.data.redis.core.*;

import java.util.Set;

/**
 * 有序集合<br>
 * 有序集合在无序集合的基础上给每个元素额外添加了一个权重<br>
 * 基于这个权重，完成对集合中元素的从小到大排序。<br>
 * 需要注意的是，虽然集合中的元素不可以重复，但权重却可以相同。<br>
 * <p/>
 * 由于拥有额外的权重信息，所以有序集合存储的不再是String类型数据
 * 而是{@link ZSetOperations.TypedTuple}的实现类或者value-score对，该接口包含两个默认成员，value、score
 * 该接口有一个默认实现类{@link DefaultTypedTuple}，该类实现了hashCode equals compareTo方法<br>
 * {@link DefaultTypedTuple#hashCode()}与value和score相关<br>
 * {@link DefaultTypedTuple#equals(Object)}则通过比较value和score<br>
 * {@link DefaultTypedTuple#compareTo(Double)}则比较score<br>
 *
 * @author liujianxin
 * @date 2018-08-31 14:00
 */

public class ZSetOpsTest extends BaseOps {

    /**
     * 基本添加操作
     */
    @Test
    public void ops1() {
        BoundZSetOperations<String, String> ops = stringTemplate.boundZSetOps("set:ZSet:A");
        // 值唯一，权重可可以相同
        ops.add("老子天下第一", 1.0);
        ops.add("老子天下第一", 1.5);
        ops.add("老子天下第1.5", 1.5);
        //该类主要重写了hashCode equals compareTo的逻辑
        //我们可以实现TypedTuple接口来写自己的业务逻辑
        //DefaultTypedTuple<String> t = new DefaultTypedTuple<>("老子宇宙第二", 2.0);
        //<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
        //set.add(t);
        //ops.add(set);
    }

    /**
     * 基本移除操作
     */
    @Test
    public void ops2() {
        BoundZSetOperations<String, String> ops = stringTemplate.boundZSetOps("set:ZSet:A");
        // 移除指定元素
        Long num = ops.remove("老子天下第一");
        // 移除指定区间内的元素
        ops.removeRange(0, -1);
        // 移除指定权重范围内的元素
        ops.removeRangeByScore(0.0, 6.6);
        System.out.println(num);
    }

    /**
     * 获取指定索引区间范围内的元素集合
     * 可只获取值(通过索引区间或者权重区间)
     * 可同时获取值和权重（通过索引区间或者权重区间）
     * <p>
     * 可反序正序获取，太多了
     */
    @Test
    public void ops4() {
        BoundZSetOperations<String, String> ops = stringTemplate.boundZSetOps("set:ZSet:A");

        /**********************通过索引区间获取***************************/
        //获取指定区间范围内的元素,当结束值为-1时，则表示（n,+∞）
        Set<String> range = ops.range(0, 5);
        //获取指定区间范围内的元素，然后反转排序。注意：redis中的元素不受影响
        Set<String> strings = ops.reverseRange(0, 2);

        //同上，不过返回值是TypedTuple
        Set<ZSetOperations.TypedTuple<String>> rws = ops.rangeWithScores(0, 2);
        rws.stream().map(ZSetOperations.TypedTuple::getValue).forEach(System.out::print);
        Set<ZSetOperations.TypedTuple<String>> rrs = ops.reverseRangeWithScores(0, 2);
        rrs.stream().map(ZSetOperations.TypedTuple::getValue).forEach(System.out::print);

        /**********************通过权重获取***************************/
        Set<String> rbs = ops.rangeByScore(0, 5.0);
        Set<String> rrbs = ops.reverseRangeByScore(0, 5.0);


    }

    /**
     * 获取元素索引/权重
     */
    @Test
    public void ops6() {
        BoundZSetOperations<String, String> ops = stringTemplate.boundZSetOps("set:ZSet:A");
        // 获取指定元素的正序排名
        Long rank = ops.rank("老子天下第一");
        // 获取指定元素的倒序排名
        Long reRank = ops.reverseRank("老子天下第一");
        //获取元素权重
        Double score = ops.score("老子天下第一");
    }

    /**
     * 权重加1
     */
    @Test
    public void ops3() {
        BoundZSetOperations<String, String> ops = stringTemplate.boundZSetOps("set:ZSet:A");
        //权重自增1哟哟哟哟哟哟
        ops.incrementScore("老子天下第一", 1.0);
    }

    /**
     * 获取集合元素数量
     */
    @Test
    public void ops5() {
        BoundZSetOperations<String, String> ops = stringTemplate.boundZSetOps("set:ZSet:A");
        //size方法底层调用zCard
        Long size = ops.size();
        Long aLong = ops.zCard();
    }

    /**
     * 并集操作，值相同的权重相加
     */
    @Test
    public void ops7() {
        BoundZSetOperations<String, String> ops = stringTemplate.boundZSetOps("set:ZSet:B");
        ops.unionAndStore("set:ZSet:A","set:ZSet:B");
    }

    /**
     * 交集操作，交集元素权重相加
     * 为什么没有差集？？？
     */
    @Test
    public void ops8(){
        BoundZSetOperations<String, String> ops = stringTemplate.boundZSetOps("set:ZSet:B");
        ops.intersectAndStore("set:ZSet:A","set:ZSet:C");
    }

    /**
     * OK,This is GOOD
     */
    @Test
    public void ops9(){
        BoundZSetOperations<String, String> ops = stringTemplate.boundZSetOps("set:ZSet:B");
        Cursor<ZSetOperations.TypedTuple<String>> scan = ops.scan(ScanOptions.NONE);
        while (scan.hasNext()){
            System.out.println(scan.next().getValue());
        }
    }

}
