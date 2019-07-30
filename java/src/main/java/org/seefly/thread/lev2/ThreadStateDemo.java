package org.seefly.thread.lev2;

import org.junit.Test;
import org.seefly.thread.ThreadUtils;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * java中的线程和操作系统中的线程是一对一映射的，可以简单的理解为java中的线程就是操作系统中的线程。
 * java线程可以分解为三部分理解
 *     1、线程元数据:名称、优先级、状态、是否守护线程...
 *     2、线程行为:启动、中断、等待...
 *     3、线程任务:即线程需要执行的任务
 * 关于线程优先级
 *     线程的优先级不能超过该线程所属线程组的优先级，并且不要将业务逻辑依赖与线程优先级，因为线程优先级依赖于操作系统，所以它并不总是按照
 *     我们想要的方式去运行。
 * 线程状态
 *      {@link Thread.State}java线程有5种状态
 *      1、NEW           属于Thread被创建，但是还没有调用start()方法启动时的状态。
 *      2、RUNNABLE      处于可运行状态的某一线程正在 Java 虚拟机中运行，但它可能正在等待操作系统中的其他资源，比如处理器。
 *      3、BLOCKED       受阻塞并且正在等待监视器锁的某一线程的线程状态。处于受阻塞状态的某一线程正在等待进入一个同步的块/方法的监视器锁，
 *                       或者在调用 Object.wait 之后再次进入同步的块/方法。
 *      4、WAITING       某一等待线程的状态，可能调用Object.wait() Thread.join LockSupport.park.
 *      5、TIMED_WAITING 具有指定等待时间的某一等待线程的状态，可能调用了，Thread.sleep() Object.wait(1)
 *                       Thread.join(1) LockSupport.parkNanos LockSupport.parkUntil
 *      6、TERMINATED    线程已经终止的状态
 * @author liujianxin
 * @date 2019-07-30 10:06
 */
public class ThreadStateDemo {

    /**
     * 线程的NEW状态只有在被创建为启动时才有，一旦启动则不会再回到NEW状态
     * 而TERMINATED状态则一旦结束，则不可能再转变为其他状态
     *
     * 处于RUNNABLE状态的线程，并不一定真正的在CPU中运行，我们只能肯定的知道
     * 它获得了除CPU时间片外的其他有所资源，至于有没有被分配时间片则不得而知。
     * 所以如果细分的话还可以分为两种子状态
     *  1、READY     已经准备好被分配时间片来运行
     *  2、RUNNING   已经被分配时间片，且正在运行
     * 但是java线程模型将这两种状态和为一种呢？因为我们能控制到的粒度只能到这种状态，至于CPU
     * 给不给分时间片那不是我们能够控制的了的。
     */
    @Test
    public void testThreadState() throws InterruptedException {
        Thread thread = ThreadUtils.newAndSleep(1000);
        // 线程启动前状态 NEW
        System.out.println(thread.getState());
        thread.start();
        // 启动后状态 RUNNABLE
        System.out.println(thread.getState());
        Thread.sleep(500);
        // thread调用sleep之后 TIMED_WAITING状态
        System.out.println(thread.getState());
        Thread.sleep(1100);
        // 运行结束 TERMINATED
        System.out.println(thread.getState());
    }

    /**
     * 三种阻塞状态{@link Thread.State}
     *  BLOCKED、WAITING、TIMED_WAITING
     * 1、BLOCKED
     *      正在等待监视器锁的线程的状态
     *   例如只有一个厕所，已经有人进去了，所以你要进入这个临界资源区只能被BLOCKED在门口。
     *   这种状态发生在对临界资源的竞争。
     * 2、WAITING
     *      处于等待状态的线程正在等待另一个线程执行特定的操作。（该状态会释放线程持有的锁）
     *   可能由于调用了Object.wait Thread.join LockSupport.park
     *   例如上饭店吃饭，点了菜之后就要等服务员上菜，这个状态跟BLOCKED的区别就是菜不是临界资源
     *   不是一道菜只能同时上给一桌，他们在不在吃这道菜跟你点不点没有关系。
     * 3、TIMED_WAITING
     *      具有指定等待时间的等待线程状态
     *    可能调用了下面的其中一种方法
     *  <ul>
     *    <li>Thread.sleep(long)</li>
     *    <li>Object#wait(long)</li>
     *    <li>Thread.join(long)</li>
     *    <li>LockSupport#parkNanos</li>
     *    <li>LockSupport#parkUntil</li>
     *  </ul>
     *  例如饭店吃饭，跟服务员点菜，说如果30分钟后还不上菜，我就不等了
     *
     */
    @Test
    public void testTreadBlocking() throws InterruptedException {
        final AtomicBoolean flag = new AtomicBoolean(true);
        Object lock = new Object();
        Thread thread = new Thread(() ->{
            while (flag.get());
            try {
                // 调用lock.wait前必须持有该对象的监视器
                // 也就是说不曾拥有，何谈失去，我真他妈是个人才
                synchronized (lock){
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true);
        });
        thread.start();
        System.out.println(thread.getState());
        flag.set(false);
        ThreadUtils.sleep(20);
        System.out.println(thread.getState());
        // 告诉等这个锁的线程现在可以醒过来抢锁了
        synchronized (lock){
            // 调用这个方法并不意味着当前线程就会释放手中的锁
            // 只是通知一下等待池中的线程进入锁池来抢锁
            lock.notify();
        }
        ThreadUtils.sleep(100);
        System.out.println(thread.getState());
    }
}
