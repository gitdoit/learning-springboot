package org.seefly.springredis.function;

import org.junit.Test;
import org.seefly.springredis.api.BaseOps;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *  思路，使用nx和ex命令参数设置一个key，当这个key不存在的时候，可以设置成功，并赋予超时时间
 *       如果锁存在，则代表已被其他线程获取，返回失败；value的值是一个可以表示锁所有者的值，一般是随机数+线程id
 *
 *       在释放锁的时候可以使用lua脚本，来原子化的判断当前锁的持有者是不是当前线程，如果是则删除，不是则什么都不做；
 *
 * 存在的问题
 *   1，不可重入
 *      用lua来判断一下？
 *     local ok = redis.call('set','lock','a','nx','get','ne',10)
 *     if ok == nil then
 *       return 'ok'
 *     end
 *     if ok == 'a' then
 *       return 'ok'
 *     end
 *     return nil
 *  2,不可重试
 *    这个加单，while(no timeout)就行了吧？
 *  3，超时释放
 *    意思是业务执行太长时间，但是设置的超时时间又太短。
 *    确实不好处理，但这种情况应该结合实际开发来，对于某些肯定不会timeout的场景用起来是没问题的
 *    但是某些业务场景不确定的话，那就需要做一些针对行的复杂处理了
 *  4 主从一致性
 *    set key的操作是写操作，会打到主节点，主从同步有需要时间
 *    当主节点宕机时，数据没同步到从节点，会丢了这个锁。
 *    这种情况一般比较少吧？并且主从同步一般耗时很短
 *    非要考虑这种情况，那你的程序肯定是需要强一致性的，何不改变一下节点同步方式？
 *
 *
 * @author liujianxin
 * @date 2022/11/13 14:49
 */
public class GlobalLock extends BaseOps {
    private static final String HOLDER_PREFIX = UUID.randomUUID().toString().replace("-","").substring(0,6)+"-";
    private static final DefaultRedisScript<Long> UN_LOCK;
    static {
        UN_LOCK = new DefaultRedisScript<>();
        UN_LOCK.setLocation(new ClassPathResource("unlock.lua"));
        UN_LOCK.setResultType(Long.class);
    }

    @Test
    public void testLock()  {
        boolean success = tryLock("test", 12);
        if(success) {
            try {
                Thread.sleep(8000);
            }catch (Exception ex) {
                unlock("test");
            }
        }
    }



    /**
     * 利用redis的 nx 命令参数，设置一个分布式锁，如果设置成果，则返回true 否则，返回false
     * 1. 对于分布式锁避免长时间使用不释放，必须要设置超时时间，避免异常情况，导致一直占有
     *
     * @param key    key
     * @param time   锁的预计使用时间
     * @return 是否成功上锁，true是，false否
     */
    public boolean tryLock(String key,long time) {
        // 设置一个value，相当于锁的版本号，可以是UUID+thread_id
        String holder = buildHolder();
        // 抢占锁
        Boolean success = this.stringTemplate.boundValueOps(buildKey(key)).setIfAbsent(holder, time, TimeUnit.SECONDS);

        return Boolean.TRUE.equals(success);
    }

    private String buildKey(String key) {
        return "lock:"+key;
    }
    private String buildHolder() {
        return HOLDER_PREFIX + Thread.currentThread().getId();
    }

    /**
     * 释放锁
     * 为了避免某些异常线程错误的释放掉其他线程的锁，这里必须先比较
     * 锁的持有者，是不是当前线程， 如果不是，则不能操作。
     * 比较这一操作必须是原子性的，不然在高并发场景下肯定会出现问题。
     * 这里可以利用lua脚本来实现
     * local value = redis.call('get',KEYS[1])
     * if value == ARGV[1] then
     *    return redis.call('del',KEYS[1])
     * end
     * return 0
     *
     */
    public void unlock(String key) {
        stringTemplate.execute(UN_LOCK, Collections.singletonList(buildKey(key)),buildHolder());
    }
}
