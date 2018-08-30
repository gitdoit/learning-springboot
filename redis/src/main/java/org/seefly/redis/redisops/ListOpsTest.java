package org.seefly.redis.redisops;

import org.junit.Test;
import org.springframework.data.redis.core.BoundListOperations;

import java.util.concurrent.TimeUnit;

/**
 * 该类用来演示redis的对list数据类型操作
 * @author liujianxin
 * @date 2018-08-30 14:51
 */
public class ListOpsTest extends BaseOps{

    /**
     * list类型基本push操作
     */
    @Test
    public void ops1(){
        BoundListOperations<String, String> ops = stringTemplate.boundListOps("list:singlePush");
        // 单个操作
        ops.leftPush("come");
        ops.rightPush("element2");
        ops.leftPush("element3");
        // 放在左边起第一个值为come的元素左边
        ops.leftPush("come","ojbk");
        // 批量操作
        BoundListOperations<String, String> bops = stringTemplate.boundListOps("list:batchPush");
        bops.leftPushAll("element","element1","element2");
        // 获取链表内的元素 第一个参数表示起始，0为第一个元素。第二个参数表示偏移量，为-1时表示全部获取
        System.out.println(ops.range(0,-1));
    }

    /**
     * 对链表进行裁剪
     */
    @Test
    public void ops2(){
        BoundListOperations<String, String> ops = stringTemplate.boundListOps("list:batchPush");
        System.out.println("裁剪前："+ops.range(0,-1));
        //保留从第一各参数开始到第二个参数之间的元素，若第二个参数为-1，则截取到为该list的长度
        ops.trim(1,-1);
        System.out.println("裁剪后："+ops.range(0,-1));
    }

    /**
     * 删除操作
     */
    @Test
    public void ops3(){
        BoundListOperations<String, String> ops = stringTemplate.boundListOps("list:batchPush");
        //移除第2次出现的指定元素
        ops.remove(2,"element");
        System.out.println("裁剪后："+ops.range(0,-1));
    }

    /**
     * 获取操作
     */
    @Test
    public void ops4(){
        BoundListOperations<String, String> ops = stringTemplate.boundListOps("list:batchPush");
        //获取第一个元素
        ops.index(0);
        //弹出左边第一个元素，弹出之后该元素将会从链表中移除
        ops.leftPop();
        // 同上，若无元素可用，则阻塞等待指定时间
        ops.leftPop(3,TimeUnit.SECONDS);
        //将sourceKey列表中的最后一个元素弹出，放到destinationKey列表的最前面，然后返回该元素的值
        stringTemplate.opsForList().rightPopAndLeftPush("list:batchPush","list:singlePush");
    }

}
