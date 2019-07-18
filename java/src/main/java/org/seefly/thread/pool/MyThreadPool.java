package org.seefly.thread.pool;

import java.math.BigDecimal;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liujianxin
 * @date 2019-07-18 09:43
 */
public class MyThreadPool {
    public static void main(String[] args) {
        BigDecimal checkCount = new BigDecimal(0.3).multiply(new BigDecimal(3014));
        double notice = checkCount.multiply(new BigDecimal(5)).setScale(3, BigDecimal.ROUND_UP).doubleValue();
        System.out.println(notice);
    }


    public static class CustomThreadPool{
        private volatile Set<Worker> workers;
        private int minSize;
        private int maxSize;
        private int keepAliveTime;
        private TimeUnit unit;
        private BlockingQueue<Runnable> workQueue;

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

    private final class Worker extends Thread{
        private Thread thread;
        private Runnable runnable;
        private boolean isNewTask;


        public Worker(Runnable runnable,boolean isNewTask){
            this.runnable = runnable;
            this.thread = this;
            this.isNewTask =isNewTask;
        }

    }
}
