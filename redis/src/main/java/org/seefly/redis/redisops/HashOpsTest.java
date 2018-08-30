package org.seefly.redis.redisops;

import org.junit.Test;
import org.springframework.data.redis.core.BoundHashOperations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liujianxin
 * @date 2018-08-30 17:14
 */
public class HashOpsTest extends BaseOps {

    /**
     * 基本添加操作
     */
    @Test
    public void ops1() {
        //这里的配置的key相当于这条数据的主键，一条数据有多个键值对
        BoundHashOperations<String, Object, Object> ops = objTemplate.boundHashOps("hash:put");
        ops.put("name", "redisForMe");
        ops.put("info", "outMan");
        ops.put("age","15");
        //批量添加
        Map<String,String>  map = new HashMap<>();
        map.put("add","China");
        map.put("phone","10010");
        ops.putAll(map);
    }

    /**
     * 基本取值操作
     */
    @Test
    public void ops2() {
        BoundHashOperations<String, Object, Object> ops = objTemplate.boundHashOps("hash:put");
        //判断该条数据中是否含有指定键值对
        if (ops.hasKey("name")) {
            //获取指定键对应的值
            System.out.println(ops.get("name"));
        }
        //同时获取多个
        System.out.println(ops.multiGet(Arrays.asList("name", "info")));
        //ops.increment()取值增加
        //获取该散列表下的key集合
        System.out.println(ops.keys());
        //索取该散列表下的value集合
        System.out.println(ops.values());
        //该散列表下的所有键值对
        ops.entries().forEach((k,v) ->{
            //do your work
        });

    }
}
