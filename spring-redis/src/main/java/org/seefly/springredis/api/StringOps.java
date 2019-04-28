package org.seefly.springredis.api;

import org.junit.Test;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 该类用来演示redis的String类型数据操作
 * 使用场景
 *  计数操作，如文章阅读量等字段
 *  数据有效期
 *
 * @author liujianxin
 * @date 2018-08-30 13:11
 */
public class StringOps extends BaseOps {
    /**
     * 基本字符串操作
     */
    @Test
    public void opt1() {
        // 绑定key，可以避免下面的每次操作都重复的绑定key
        BoundValueOperations<String, String> ops = stringTemplate.boundValueOps("string:set");
        // 由于上面已经指定了key，这里可以直接放入value。若该key已有对应的value，则会覆盖
        // order --> set string:set hello
        ops.set("hello ");
        // 从指定偏移量开始覆盖该key对应的value。小坑~~~~~~多一位
        ops.set(" redis", 6);
        //追加，若不存在则创建
        ops.append("!");
        ops.setIfAbsent("set如果不存在");
        System.out.println(ops.get());
        //返回该键对应的值的长度
        //ops.size();
        //见名知意
        //ops.getAndSet()

        // order --> del string:set
        //stringTemplate.delete("string:set");
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
     *
     * 还有一个需要特别注意的地方是如果一个字符串已经设置了过期时间，然后你调用了 set 方法修改了它，它的过期时间会消失。
     */
    @Test
    public void opt2() throws InterruptedException {
        BoundValueOperations<String, String> ops = stringTemplate.boundValueOps("string:timer1");
        //5秒后删除
        //ops.set("delete me later", 20, TimeUnit.MINUTES);
        System.out.println(ops.getExpire());
        System.out.println("计时开始:" + ops.get());
        Thread.sleep(5000);
        System.out.println("5秒计时结束:" + ops.getExpire());
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
        stringTemplate.opsForValue().multiSet(kv);
        //template.opsForValue().multiSetIfAbsent();


        List<String> keys = new ArrayList<>(2);
        keys.add("string:multiSet:key1");
        keys.add("string:multiSet:key2");
        //同时获取多个值
        List<String> values = stringTemplate.opsForValue().multiGet(keys);
        for(String value : values){
            System.out.println(value);
        }


    }

    /**
     * 支持对整数的自增和double的自增
     */
    @Test
    public void opt4() {
        BoundValueOperations<String, String> ops = stringTemplate.boundValueOps("string:longInc");
        ops.set("2333");
        System.out.println("自增前："+ops.get());
        ops.increment(4333);
        System.out.println("自增后："+ops.get());
    }

    /**
     * 演示bit操作
     * 对指定位置上的比特位进行变换
     * 0 -->48 --> 0011 0000
     * 1 -->49 --> 0011 0001
     */
    @Test
    public void opt5(){
        ValueOperations<String, String> opsForValue = stringTemplate.opsForValue();
        opsForValue.set("string:bit","0");
        //把0的二进制第7位变为1
        opsForValue.setBit("string:bit",7,true);
        System.out.println(opsForValue.get("string:bit"));
    }
}
