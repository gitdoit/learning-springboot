package org.seefly.juc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author liujianxin
 * @date 2018-11-22 09:43
 */
public class ThreadPool {

    /**
     * 这几种形式的线程池，它们封装的是java.util.concurrent.ThreadPoolExecutor
     * 最主要的区别在于使用的任务队列和决绝策略
     * 1、直接提交的队列：由SynchronousQueue提供，他是一个特殊的阻塞队列。
     * 他没有容量，每一个插入操作都要等待一个响应的删除操作，反之亦然。
     * 提交的任务不会被它保存，而是将新任务提交给线程执行，若没有空闲线程，则创建新的线程。
     * 若线程达到最大，则执行拒绝策略。CachedThreadPool使用的就是这种。所以可以看见它的核心线程数
     * 是0，而最大线程数为Integer.MAX_VALUE，说明只要向这个线程提交任务，都会被执行。直到耗尽系统资源。
     *
     * 2、有界的任务队列：ArrayBlockingQueue，它的构造必须指定一个容量，当使用它作为任务队列时，若有
     * 新的任务，且线程池中的线程数小于corePoolSize，则会直接创建并执行，若大于，则会加入任务队列。如果
     * 任务队列已满，则无法加入。此时若总线程数不大于maximumPoolSize，则创建新的线程执行任务。若大于，则执行拒绝
     * 策略。总之，只有在ArrayBlockingQueue满时才会将线程数提升到corePoolSize以上。
     *
     * 3、无界的任务队列：LinkedBlockingQueue，它没有容量限制，这说明线程池的线程数永远不会大于corePoolSize.
     * 当新任务到来时，若线程池线程数小于corePoolSize则直接创建新线程，若大于则放入任务队列。由于任务队列无界，
     * 所以永远不会出现决绝执行的情况。
     *
     * 4、有限任务队列：PriorityBlockingQueue，可以控制任务的执行顺序，它是一个特殊的无界队列。
     * 上面三个任务队列都是FIFO的，而这个则是根据任务优先级来执行任务。
     *
     */
    public static void main(String[] args){
        Executors.newFixedThreadPool(1);
        Executors.newCachedThreadPool();
        Executors.newSingleThreadExecutor();
        Executors.newWorkStealingPool(1);
    }


    public void testScheduledThreadPool(){
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        // 以固定频率执行任务，上一个任务开始的时间点+第三个参数值 = 下一个任务的开始执行时间点
        // 如果任务执行的时间太长了，超过了第三个参数值，那么在上一个任务执行完毕后立即执行下一个
        service.scheduleAtFixedRate(()->{
            try {
                Thread.sleep(2);
                System.out.println(System.currentTimeMillis()/1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },0,3, TimeUnit.SECONDS);


        // 上一个任务执行完毕的时间点 + 参数三的值 = 下一个任务开始执行的时间点
        service.scheduleWithFixedDelay(() ->{
            try {
                Thread.sleep(2);
                System.out.println(System.currentTimeMillis()/1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },0,2,TimeUnit.SECONDS);
    }

}
