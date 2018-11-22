package org.seefly.thread;

/**
 * 描述信息：Volatile关键字的作用
 * <p>
 * 1.
 * 由于多线程环境先，当同时有多个线程对同一个变量进行操作，那么都是从内存中读取该变量的值，到cpu的高速缓存中。
 * 然后在高速缓存中对此变量副本进行操作，之后才会从新写入到主存中。而什么时候写入主存是不确定的，这就造成了一种现象叫做不可见性。
 * 而volatile关键字则会使这种操作变为可见的，也就说一旦一个线程对一变量进行 了修改，那么会立即写入主存，同时使其他线程中的变量副本变为失效的，
 * 致使他们从新到主存中读取新的值。所以保证其他线程读取到的都是最新的。
 *
 * 2.
 * volatile关键并不会杜绝丢失更新的现象。因为volatile关键字只保证当主存中的值被修改了之后才会通知到各个修改本副本。
 * 那么当其中一个线程在修改本地副本的时候，还没有来得及将修改变量更新到主存就是去了cpu资源，然后其他线程对这个变量
 * 进行了修改，那么这个线程修改完成之后，之前的一个线程再从新写入没有来得及写入的数据就会造成丢失更新。
 *
 * 3.
 * volatile关键字值保证各个线程中存储的被volatile关键字修饰的变量的副本和主存当中的一致。
 * 但各个线程修改自己本地变量以及更新到主存并不是一个原子操作。
 *
 * AtomicInteger
 * @author liujianxin
 * @date 2018-07-30 10:50
 **/
public class B8_Volatile {

    public static void main(String[] args)throws Exception{
        new ThreadVolatile().start();
        new ThreadVolatile().start();

        new ThreadNoVlolatile().start();
        new ThreadNoVlolatile().start();

        Thread.sleep(2000);

        System.out.println("volatile int:"+ThreadVolatile.getCount());
        System.out.println("No volatile int:"+ThreadNoVlolatile.getCount());
    }

    /**
     * 演示多个线程同时操作被volatile关键字修饰的计数器
     */
    static class ThreadVolatile extends Thread{
        private volatile static int count = 0;
        @Override
        public void run() {
            while (count < 10000){
                count ++;
            }
        }

        public static int getCount(){
            return count;
        }
    }

    static class ThreadNoVlolatile extends Thread{
        private static int count = 0;
        @Override
        public void run() {
            while (count < 10000){
                count ++;
            }
        }

        public static int getCount(){
            return count;
        }
    }


}
