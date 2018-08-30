package org.seefly.redis.redisops;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 该类用来演示redis的String类型数据操作
 *
 * @author liujianxin
 * @date 2018-08-30 13:11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StringOps {
    @Autowired
    private RedisTemplate<String, String> template;

    /**
     * 基本字符串操作
     */
    @Test
    public void opt1() {
        // 绑定key，可以避免下面的每次操作都重复的绑定key
        BoundValueOperations<String, String> ops = template.boundValueOps("string:set");
        // 由于上面已经指定了key，这里可以直接放入value。若该key已有对应的value，则会覆盖
        ops.set("hello ");
        // 从指定偏移量开始覆盖该key对应的value
        ops.set("redis", 7);
        //追加
        ops.append("!");
        ops.setIfAbsent("set如果不存在");
        System.out.println(ops.get());
    }

    /**
     * 演示定时操作
     * redis并不是为每一个使用了定时删除的键都配置一个线程进行计时
     *
     * redis采用的是定期删除+惰性删除策略。
     * 定期删除，redis默认每个100ms检查，是否有过期的key,有过期key则删除。
     * 需要说明的是，redis不是每个100ms将所有的key检查一次，而是随机抽取进行检查(如果每隔100ms,全部key进行检查，redis岂不是卡死)。
     * 因此，如果只采用定期删除策略，会导致很多key到时间没有删除。
     * 于是，惰性删除派上用场。也就是说在你获取某个key的时候，redis会检查一下，这个key如果设置了过期时间那么是否过期了？如果过期了此时就会删除。
     */
    @Test
    public void opt2() throws InterruptedException {
        BoundValueOperations<String, String> ops = template.boundValueOps("string:timer");
        //5秒后删除
        ops.set("delete me later", 5, TimeUnit.SECONDS);
        System.out.println("计时开始:" + ops.get());
        Thread.sleep(5000);
        System.out.println("5秒计时结束:" + ops.get());
    }

    /**
     * 多键值操作
     */
    @Test
    public void opt3(){
        Map<String,String> kv = new HashMap<>(2);
        kv.put("string:multiSet:key1","value1");
        kv.put("string:multiSet:key2","value2");
        //同时放入多个键值对
        template.opsForValue().multiSet(kv);


        List<String> keys = new ArrayList<>(2);
        keys.add("string:multiSet:key1");
        keys.add("string:multiSet:key2");
        //同时获取多个值
        List<String> values = template.opsForValue().multiGet(keys);
        for(String value : values){
            System.out.println(value);
        }
    }


}
