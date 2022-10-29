package org.seefly.springredis.api;

import org.junit.Test;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author liujianxin
 * @date 2022/10/27 16:58
 */
public class LuaOps extends BaseOps{


    /**
     * 使用lua脚本,实现一个定长队列
     */
    @Test
    public void testLua(){

        String script =
                "local list = redis.call('LRANGE',KEYS[1],0,3)\n" +
                "local size = redis.call('lpush',KEYS[1],ARGV[1])\n" +
                "if size > 4 then\n" +
                "    redis.call('rpop',KEYS[1])\n" +
                "end\n" +
                "return list";

        RedisSerializer<String> stringSerializer = this.stringTemplate.getStringSerializer();
        byte[] key = stringSerializer.serialize("GAS-TEST-KEY");
        byte[] value = stringSerializer.serialize("32.1212:121.1212:433434");

        byte[] SCIRPT = stringSerializer.serialize(script);
        List<byte[]> execute = this.stringTemplate.execute((RedisCallback<List<byte[]>>) (connection) -> {
            return connection.eval(SCIRPT, ReturnType.MULTI, 1, key, value);
        });
        execute.stream().map(String::new).forEach(System.out::println);

    }

    @Test
    public void testLuaZSet() {
        String script =
                // 获取过去指定时间窗口的数据
                "local list = redis.call('ZRANGEBYSCORE',KEYS[1],tonumber(ARGV[1]),tonumber(ARGV[2]))\n" +
                // 删除窗口之外的数据
                "redis.call('ZREMRANGEBYSCORE',KEYS[1],0,tonumber(ARGV[1]))\n" +
                // 添加新数据
                "redis.call('ZADD',KEYS[1],tonumber(ARGV[2]),ARGV[3])\n" +
                "return list";

        RedisSerializer<String> stringSerializer = this.stringTemplate.getStringSerializer();
        long time = System.currentTimeMillis();
        byte[] key = stringSerializer.serialize("GAS-TEST-KEY-WINDOW");
        byte[] value = stringSerializer.serialize("47"+":"+time);
        byte[] start = stringSerializer.serialize((time - 60 * 1000) + "");
        byte[] end = stringSerializer.serialize(time + "");

        byte[] SCRIPT = stringSerializer.serialize(script);
        Assert.notNull(SCRIPT,"脚本不能为空");
        List<byte[]> execute = this.stringTemplate.execute((RedisCallback<List<byte[]>>) (connection) -> connection.eval(SCRIPT, ReturnType.MULTI, 1, key, start,end,value));
        execute.stream().map(String::new).forEach(System.out::println);

    }
}
