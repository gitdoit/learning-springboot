package org.seefly.redis.redisops;

import org.junit.Test;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

/**
 *
 * 有序集合<br>
 * 有序集合在无序集合的基础上给每个元素额外添加了一个权重<br>
 * 基于这个权重，完成对集合中元素的从小到大排序。<br>
 * 需要注意的是，虽然集合中的元素不可以重复，但权重却可以相同。<br>
 * <p/>
 * 由于拥有额外的权重信息，所以有序集合存储的不再是String类型数据
 * 而是{@link ZSetOperations.TypedTuple}的实现类，该接口包含两个默认成员，value、score
 * 该接口有一个默认实现类{@link DefaultTypedTuple}，该类实现了hashCode equals compareTo方法<br>
 *  {@link DefaultTypedTuple#hashCode()}与value和score相关<br>
 *  {@link DefaultTypedTuple#equals(Object)}则通过比较value和score<br>
 *  {@link DefaultTypedTuple#compareTo(Double)}则比较score<br>
 * @author liujianxin
 * @date 2018-08-31 14:00
 */

public class ZSetOpsTest extends BaseOps {

    @Test
    public void ops1() {
        BoundZSetOperations<String, String> ops = stringTemplate.boundZSetOps("set:ZSet:A");
        //该类主要重写了hashCode equals compareTo的逻辑
        //我们可以实现TypedTuple接口来写自己的业务逻辑
        DefaultTypedTuple<String> t = new DefaultTypedTuple<>("", 2.0);
    }
}
