package org.seefly.springredis.api;

import org.junit.Test;
import org.seefly.springredis.model.User;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用场景
 * 对结构化数据存储，例如存储用户信息的某些字段
 * 将一个javaBean缓存到redis中
 * 使用其中某个唯一字段作为其主键，其他属性以键值对的形式存放
 * 这样可以使用这个唯一字段获取到这个被缓存的javaBean
 * 或者使用唯一主键字段+字段名获取到指定的属性值
 * 这个数据类型用起来和关系型数据库很像
 * <p>
 * 散列可以存储多个键值对之间的映射，和字符串一样，散列存储的值既可以是字符串又可以是数字
 * 并且用户同样可以对散列存储的数据执行自增/减操作
 *
 * @author liujianxin
 * @date 2018-08-30 17:14
 */
public class HashOps extends BaseOps {


    /**
     * 基本添加操作
     */
    @Test
    public void ops1() {
        //这里的配置的key相当于这条数据的主键，一条数据有多个键值对
        BoundHashOperations<String, Object, Object> ops = objTemplate.boundHashOps("hash:put");
        //hset hash:put name redisForMe
        ops.put("name", "redisForMe");
        ops.put("info", "outMan");
        ops.put("age", "15");
        //批量添加
        Map<String, String> map = new HashMap<>();
        map.put("add", "China");
        map.put("phone", "10010");
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
            //获取指定键对应的值 hget hash:put name
            System.out.println(ops.get("name"));
            //删除指定的key-value对
            //ops.delete("name");
        }
        //同时获取多个
        System.out.println(ops.multiGet(Arrays.asList("name", "info")));
        //ops.increment()取值增加
        //获取该散列表下的key集合
        System.out.println(ops.keys());
        //索取该散列表下的value集合
        System.out.println(ops.values());
        //该散列表下的所有键值对 hgetall hash:put
        ops.entries().forEach((k, v) -> {
            //ops.delete(k);
        });


    }

    /**
     * 遍历操作
     */
    @Test
    public void ops3() {
        BoundHashOperations<String, Object, Object> ops = objTemplate.boundHashOps("hash:put");
        Cursor<Map.Entry<Object, Object>> scan = ops.scan(ScanOptions.scanOptions().count(2).match("info").build());
        while (scan.hasNext()) {
            Map.Entry<Object, Object> next = scan.next();
            System.out.println(scan.getCursorId());
            System.out.println(scan.getPosition());
            System.out.println(next.getKey() + ":" + next.getValue());
        }
    }

    @Test
    public void ops4() {
        User u = new User();
        u.setName("刘");
        u.setAge(25);
        u.setId("1536113167660");
        BoundHashOperations<String, String, User> ops = objTemplate.boundHashOps("hash:object");
        ops.put(u.getId(), u);
    }

    @Test
    public void ops5() {
        BoundHashOperations<String, String, String> ops = objTemplate.boundHashOps("hash:put");
        List<String> objects = ops.multiGet(Arrays.asList("name", "info", "shasdsf"));
        for (String info : objects) {
            System.out.println(info);
        }
    }

    @Test
    public void testIncr() {
        //
        objTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        objTemplate.boundValueOps("BOX-COUNT:UID:"+null).increment(1);
    }

}
