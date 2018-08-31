package org.seefly.redis.redisops;

import org.junit.Test;
import org.springframework.data.redis.core.BoundSetOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * set数据结构是String类型的无序集合
 * 也就是说每个集合中的成员都是唯一的，不能出现重复数据。
 * 实现方式和java中的HashSet类似，都是使用HahsMap作为底层的数据结构，只使用key，value全为null
 * 基于此，我们使用redis的这种结构和使用HashSet<String>的思路基本一样。
 *
 * @author liujianxin
 * @date 2018-08-31 11:15
 */
public class SetOpsTest extends BaseOps {

    /**
     * 基本添加操作
     */
    @Test
    public void ops1(){
        BoundSetOperations<String, String> ops = stringTemplate.boundSetOps("set:A");
        //并集操作,返回新增元素个数，若所有元素都已存在，则返回0
        Long add = ops.add("e1", "e2", "e3");
        System.out.println(add);
    }

    /**
     * 基本移除操作
     */
    @Test
    public void ops2(){
        BoundSetOperations<String, String> ops = stringTemplate.boundSetOps("set:A");
        //移除n个指定元素，返回成功移除的元素个数
        Long e1 = ops.remove("e1");
        //移动指定的元素到另一个集合中
        Boolean move = ops.move("set:B", "e1");
    }

    /**
     * 基本获取操作
     */
    @Test
    public void  ops5(){
        BoundSetOperations<String, String> ops = stringTemplate.boundSetOps("set:A");
        //获取集合中所有的元素
        Set<String> all = ops.members();
        //随机获取集合中的一个元素
        String ran = ops.randomMember();
        //随机弹出一个集合中的元素
        String pop = ops.pop();
        //随机获取集合中的n个元素,同一个元素可能会被重复获取
        List<String> randomList = ops.randomMembers(4);
        //随机获取集合中的N个不重复元素
        Set<String> distinctRandomMembers = ops.distinctRandomMembers(4);
    }

    /**
     * 集合求交集操作
     */
    @Test
    public void ops3(){
        BoundSetOperations<String, String> ops = stringTemplate.boundSetOps("set:B");
        ops.add("e1","a1","a2","a3");
        // 两个集合的交集，并返回该交集.被操作的集合并不会发生任何变更
        Set<String> intersect = ops.intersect("set:A");
        System.out.println(intersect.size());
        // 求两个集合的交集，并将结果放入另一个集合
        ops.intersectAndStore("set:A","set:C");
        //多个集合求交集，并将结果放入另一个集合
        //ops.intersectAndStore(keys,targetSet);
        //多个集合求交集，并将结果返回
        //Set<String> set = ops.intersect(keys);
    }

    /**
     * 并集操作
     * 并集操作分为两种，一种是有返回值，一种是无返回值
     * 无返回值是将操作结果放入redis中
     * 有返回值则是将操作结果返回
     */
    @Test
    public void ops4(){
        BoundSetOperations<String, String> ops = stringTemplate.boundSetOps("set:union:A");
        // 两个集合求并集，并将结果返回
        Set<String> union = ops.union("set:union:B");
        // 多个集合求并集，并将结果返回
        // Set<String> union =ops.union(keys)

        //两个集合做并集，并将结果放在另一个集合中
        ops.unionAndStore("set:union:B","set:union:C");
        //多个集合做并集，并将结果存储在另一个集合中
        //ops.unionAndStore(Arrays.asList("set:union:B"),"set:union:C");
    }

    /**
     * 差集操作
     */
    @Test
    public void osp4(){
        BoundSetOperations<String, String> ops = stringTemplate.boundSetOps("set:diff:A");
        //两个集合做差集，并将差集结果返回
        Set<String> diffB = ops.diff("set:diff:B");
        //多个集合做差集，并将差集结果返回，即A和B的差集结果再和C做差集
        Set<String> diffBC = ops.diff(Arrays.asList("set:diff:B", "set:diff:C"));
        //两个集合做差集，并将结果存放到另一个集合中
        ops.diffAndStore("set:diff:B","set:diff:D");
        //多个集合做差集，并将结果存放到另一个集合中
        ops.diffAndStore(Arrays.asList("set:diff:B", "set:diff:C"),"set:diff:D");
    }
}
