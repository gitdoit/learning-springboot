package org.seefly.springredis.function;

import org.junit.Test;
import org.seefly.springredis.api.BaseOps;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;


/**
 * 分布式全局自增编号
 * @author liujianxin
 * @date 2022/11/12 18:06
 */
public class GlobalIncrementID extends BaseOps {
    private static final long START_TIMESTAMP = 1640966400;


    /**
     * 并发测试，300个线程，每个线程循环100次， i7-12700下 1470ms左右
     */
    @Test
    public void testIncrementId() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(500,500,10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));

        long start =  System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(300);
        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                getId("test");
            }
            latch.countDown();
        };


        for (int i = 0; i < 300; i++) {
            executorService.submit(task);
        }

        latch.await();
        System.out.println(System.currentTimeMillis() - start);
    }


    /**
     * 根据key获取一个全局自增的id，直接是使用redis的自增命令 会导致得到的id有明显的业务量痕迹，这里使用
     * 分段的形式来拼接这个id
     * 对于一个 64bit的long，高32位存储一个当前时间距指定时间锚点的偏移量（秒），低32位存储该key当天的自增数量
     */
    public long getId(String key) {
        // 全局自增id，使用 long 来表示全局自增id
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long increment = stringTemplate.boundValueOps(key + ":" + format).increment();
        // 高32位存储当前时间距锚点时间的偏移量，秒
        long diff = System.currentTimeMillis() / 1000 - START_TIMESTAMP;
        // 低32位存储当天自增数量
        return diff << 32 | increment;
    }

}
