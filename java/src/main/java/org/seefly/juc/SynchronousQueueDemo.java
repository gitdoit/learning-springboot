package org.seefly.juc;

import java.util.concurrent.*;

/**
 *
 * 阻塞队列，其中每个插入操作必须等待另一个线程执行相应的删除操作，反之亦然。
 * 同步队列没有任何内部容量，甚至没有容量。您无法查看同步队列，因为只有在您尝试删除元素时才会出现该元素;
 * 你不能插入一个元素（使用任何方法），除非另一个线程试图删除它;
 * 你不能迭代，因为没有什么可以迭代。
 * 队列的头部是第一个排队插入线程试图添加到队列的元素;如果没有这样的排队线程，那么没有元素可用于删除，poll（）将返回null。
 * 出于其他Collection方法（例如contains）的目的，SynchronousQueue充当空集合。
 * 此队列不允许null元素。,
 * 同步队列类似于CSP和Ada中使用的集合点通道.它们非常适用于切换设计，其中在一个线程中运行的对象必须与在另一个线程中运行的对象同步，
 * 以便将其传递给某些信息，事件或任务。
 * 此类支持用于排序等待生产者和消费者线程的可选公平策略。
 * 默认情况下，不保证此顺序。但是，将fairness设置为true构造的队列以FIFO顺序授予线程访问权限
 *
 *
 * 代码来自：https://stackoverflow.com/questions/5102570/implementation-of-blockingqueue-what-are-the-differences-between-synchronousque#
 * 该类演示了阻塞队列  队列长度  吞吐量的关系
 * @author liujianxin
 * @date 2018-11-20 15:04
 */
public class SynchronousQueueDemo {
    static ExecutorService e = Executors.newFixedThreadPool(2);
    static int N = 1000000;

    public static void main(String[] args) throws Exception {
        System.out.println("QueueSize\tLinked\tArray\tSync");
        for (int i = 0; i < 10; i++) {
            int length = (i == 0) ? 1 : i * 5;
            System.out.print(length + "\t");
            System.out.print(doTest(new LinkedBlockingQueue<Integer>(length), N) + "\t");
            System.out.print(doTest(new ArrayBlockingQueue<Integer>(length), N) + "\t");
            System.out.print(doTest(new SynchronousQueue<Integer>(), N));
            System.out.println();
        }

        e.shutdown();
    }

    /**
     *
     * @param q
     * @param n
     * @return
     * @throws Exception
     */
    private static long doTest(final BlockingQueue<Integer> q, final int n) throws Exception {
        long t = System.nanoTime();

        e.submit(() -> {
            for (int i = 0; i < n; i++){
                try { q.put(i); } catch (InterruptedException ex) {}
            }
        });

        Long r = e.submit(() -> {
            long sum = 0;
            for (int i = 0; i < n; i++){
                try { sum += q.take(); } catch (InterruptedException ex) {}
            }
            return sum;
        }).get();
        t = System.nanoTime() - t;
        // Throughput, items/sec
        return (long)(1000000000.0 * N / t);
    }
}
