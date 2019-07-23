package org.seefly.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liujianxin
 * @date 2019-07-18 09:43
 */
@Slf4j
public class MyThreadPool {
    public static void main(String[] args) {
        MyThreadPool pool = new MyThreadPool(3,5,5,TimeUnit.SECONDS,new ArrayBlockingQueue<>(4));
        pool.execute(()->{
            System.out.println(1);
        });


    }

    private final ReentrantLock lock = new ReentrantLock();
    /**工作线程*/
    private volatile Set<Worker> workers;
    /**核心线程数*/
    private int minSize;
    /**最大线程数*/
    private int maxSize;
    /**超出线程存活时间*/
    private int keepAliveTime;
    private TimeUnit unit;
    /**任务队列*/
    private BlockingQueue<Runnable> workQueue;
    /**线程池是否已经关闭*/
    private AtomicBoolean isShutDown = new AtomicBoolean(false);
    /**任务总数*/
    private AtomicInteger totalTask = new AtomicInteger();


    public MyThreadPool( int minSize, int maxSize, int keepAliveTime, TimeUnit unit,BlockingQueue<Runnable> workQueue) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.workQueue = workQueue;

        this.workers = new ConcurrentHashSet<>();
    }

    public void execute(Runnable runnable){
        if (runnable == null) {
            throw new NullPointerException("Runnable can not be null!");
        }
        if(isShutDown.get()){
            log.warn("线程池已经关闭！");
            return;
        }
        // 任务
        totalTask.incrementAndGet();
        // 若当前线程数小于核心线程数，则可以直接创建心线程，放入线程池
        if(workers.size() < minSize){
            addWorker(runnable);
            return;
        }
        // 否则，放入阻塞队列
        boolean offer = workQueue.offer(runnable);
        // 若阻塞队列塞不下了
        if(!offer){
            // 若当前线程数，小于最大线程数，可以创建新的线程
            if(workers.size() < maxSize){
                addWorker(runnable);
                return;
            }
            // 执行拒绝执行策略
            log.warn("线程数已最大，队列已满..执行拒绝策略！");
        }

    }

    /**
     * 执行给定任务，并将创建的线程放到线程队列中去
     */
    private void addWorker(Runnable runnable){
        Worker worker = new Worker(runnable,true);
        worker.startTask();
        workers.add(worker);
    }


    /**
     * 立即关闭线程池
     * 这样会造成任务丢失
     */
    private void shutDownNow(){
        isShutDown.set(true);
        tryClose(false);
    }

    /**
     * 尝试关闭线程池
     * @param isTry 是否直接关闭
     */
    private void tryClose(boolean isTry){
        if(!isTry){
            closeAll();
        }else if(isShutDown.get() && totalTask.get() == 0){
            closeAll();
        }
    }

    /**
     * 关闭当前线程队列中所有的线程
     */
    private void closeAll(){
        for(Worker worker : workers){
            worker.close();
        }
    }

    /**
     * 获取当前线程池线程数
     * @return 当前线程池线程数
     */
    public int getWorkerCount(){
        return this.workers.size();
    }

    /**
     * 从任务队列中获取任务
     * 1、若当前线程池已经关闭，或者任务队列中没有任务，返回null
     * 2、若当前线程数大于核心线程数，则需要在规定阻塞时间内获取到任务，否则返回null
     * 3、若当前线程数小于核心线程数，则当前线程可以无限期阻塞直到获取到任务
     */
    private Runnable getTask(){
        // 若线程池已经关闭，或者任务全都完成了
        if(isShutDown.get() && totalTask.get() == 0){
            return null;
        }
        lock.lock();
        Runnable task;
        try{
            // 如果超出核心线程数，就需要对额外线程进行超时限定
            if(workers.size() > minSize){
                task = workQueue.poll(keepAliveTime,unit);
            }
            // 若是核心线程想获取任务执行，就可以无限期等待
            else {
                task = workQueue.take();
            }
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.unlock();
        }
        return task;
    }




    /***********************************************************************************************************/
    private final class Worker extends Thread{
        private Thread thread;
        private Runnable task;
        private boolean isNewTask;


        public Worker(Runnable runnable,boolean isNewTask){
            this.task = runnable;
            this.thread = this;
            this.isNewTask =isNewTask;
        }


        /**
         * 工作线程的Run方法
         * 1、判断是否为新创建的线程，如果是，则直接运行其构造方法中传入的Runnable
         * 2、如果不是，则从任务队列中获取任务去执行
         * 3、如果没有从任务队列中获取到任务，那么有两种情况
         *    1、当前线程不是核心线程，在指定的存活时间内没有等到任务
         *    2、当前线程是核心线程，线程池关闭了，且任务已经执行完毕
         *    这种情况下需要关闭当前线程，即将当前线程从线程队列中移除
         */
        @Override
        public void run(){
            Runnable task = null;
            // 是新起的线程，就这样？为啥
            if(isNewTask){
                task = this.task;
            }
            try {
                // 若为新起的线程就直接执行，或者从任务队列中拿到的任务也直接执行
                while (task != null || (task = getTask()) != null){
                    try{
                        task.run();
                    }finally {
                        task = null;
                        // 执行完毕，待执行任务总数减一
                        int count = totalTask.decrementAndGet();
                        if(count == 0){
                            // 没有任务了
                            log.info("没有任务了");
                        }
                    }

                }
            }finally {
                // 额外的线程在指定时间内获取不到任务了
                // 就把自己从线程池中移除
                workers.remove(this);
                // 每当一个线程执行完毕，都尝试关闭线程池
                tryClose(true);
            }
        }


        public void startTask(){
            log.info("开始执行任务...");
            this.thread.start();
        }

        public void close(){
            this.thread.interrupt();
        }

    }





    private final class ConcurrentHashSet<T> extends AbstractSet<T> {
        private final ConcurrentHashMap<T,Object> holder = new ConcurrentHashMap<>();
        private final Object PRESENT = new Object();
        private final AtomicInteger ADDER  = new AtomicInteger();


        @Override
        public boolean add(T t) {
            ADDER.incrementAndGet();
            return holder.put(t,PRESENT) == PRESENT;
        }

        @Override
        public boolean remove(Object o) {
            ADDER.decrementAndGet();
            return holder.remove(o) == PRESENT;
        }

        @Override
        public Iterator<T> iterator() {
            return holder.keySet().iterator();
        }

        @Override
        public int size() {
            return ADDER.get();
        }
    }


}
