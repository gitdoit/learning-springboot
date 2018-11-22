package org.seefly.juc.atomic;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 就是一个带版本号的乐观锁
 * 它里面的时间戳就是相当于版本号
 * 期望值、期望版本号相同才会修改
 * 相较于{@link AtomicReference},这个类由于有版本号，所以可以记录状态。即被改变了几次
 * 例如AtomicReference<Integer> ，被N个线程改来改去，最后又变为最初的那个值。
 * 这时候可能有些线程没有发觉到它有过变化，这种情况是在某些情景下是不被允许的
 * @author liujianxin
 * @date 2018-11-19 19:40
 */
public class AtomicStampedReferenceDemo {
    private static AtomicStampedReference<Integer> asr = new AtomicStampedReference<>(0, 1);

    public static void main(String[] args) throws InterruptedException {
        // 很明显多线程并发修改，只有一个可以修改成功
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                Integer localValue = asr.getReference();
                int version = asr.getStamp();
                Integer newValue = null;
                // do some job
                try {
                    newValue = localValue + 1;
                    Thread.sleep(RandomUtils.nextLong(0,200));
                }catch (Exception e){
                    e.printStackTrace();
                }
                boolean flag = asr.compareAndSet(localValue, newValue, version, version + 1);
                System.out.println("线程："+Thread.currentThread().getName()+"|期望值："+localValue+"|期望版本号:"+version+"|变更："+flag);
            }).start();
        }
        Thread.sleep(1000);
    }
}
