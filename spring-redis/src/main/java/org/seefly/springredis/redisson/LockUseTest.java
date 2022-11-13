package org.seefly.springredis.redisson;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.seefly.springredis.api.BaseOps;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author liujianxin
 * @date 2022/11/13 17:07
 */
@Slf4j
public class LockUseTest extends BaseOps {
    @Resource
    private RedissonClient client;

    /**
     * 使用redisson的可重入锁
     *  原理是使用lua脚本，来执行获取锁的操作，他这个锁也是实现了java的{@link Lock}接口；
     *  具体原理是使用一个hash结构，key是线程id，value是重入次数，靠lua保证原子性，靠超时时间保证锁会被释放；
     *  感觉跟java的可重入锁实现原理差不多，都是用一个计数器标记重入次数
     */
    @Test
    public void testUseRedissonLock() throws InterruptedException {
        RLock rtest = client.getLock("rtest");
        boolean success = rtest.tryLock(1, 30, TimeUnit.SECONDS);
        if (success) {
            try {
                log.info("[redisson] 成功获取redisson锁");
                Thread.sleep(20000);
            } finally {
                rtest.unlock();
            }

        }
    }
}
