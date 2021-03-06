package org.seefly.springredis.pipeline;

import org.junit.Test;
import org.seefly.springredis.api.BaseOps;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import java.util.List;

/**
 * redis的管道操作，类似jdbc的批量操作
 * 批量处理数据提升应用性能。
 * @author liujianxin
 * @date 2018-11-01 17:25
 */
public class PipeOps extends BaseOps {

    @Test
    public void testPipe1(){
        stringTemplate.executePipelined(new RedisCallback<List<String>>() {
            @Override
            public List<String> doInRedis(RedisConnection connection) throws DataAccessException {

                return null;
            }
        });
    }


    @Test
    public void testPipe(){
        List<Object> results = stringTemplate.executePipelined(
                (RedisCallback<Object>) connection -> {
                    StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
                    for (int i = 0; i < 100; i++) {
                        stringRedisConn.lPush("key", "" + i);
                    }
                    //不能手动关闭连接
                    //stringRedisConn.close();
                    //只能返回null,因为返回值会在服务器执行完命令后被覆盖
                    return null;
                });
        //返回值和执行命令的顺序是对应的
        System.out.println(results.size());
    }
}
