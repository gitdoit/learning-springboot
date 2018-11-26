package org.seefly.juc.locks;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.locks.StampedLock;

/**
 * 一种改进的读写锁，使用乐观的读策略，这种策略使得写完全不会阻塞线程。
 *
 *  该例来自{@link StampedLock}的注释文档，描述了如何使用这个锁在并发环境下读取、修改一个二维平面上一个点的坐标。
 * @author liujianxin
 * @date 2018-11-26 10:03
 */
public class StampedLockDemo {
    /**带有版本号的乐观锁*/
    private StampedLock lock = new StampedLock();
    /**临界资源点*/
    private double x, y;

    /**
     * 移动这个点到指定坐标
     */
    private void move(double deltaX, double deltaY) {
        // 加写锁会使锁的版本号自增1
        long stamp = lock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            // 释放写锁 也会使版本号自增1
            lock.unlockWrite(stamp);
        }
    }

    /**
     * 获取这个点到原点的距离
     */
   private double distanceFromOrigin() {
        // 尝试获取乐观锁
        long stamp = lock.tryOptimisticRead();
        // 读取临界资源
        double currentX = x, currentY = y;
        // 查看在读取的过程中，临界区资源释放被其他线程修改
        // 若被修改，则使用悲观锁，阻塞写操作。重新读取临界区资源
        // 或者可以使用循环+CAS读取
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }


    private void randomSleep(){
        try {
            Thread.sleep(RandomUtils.nextLong(500,2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
