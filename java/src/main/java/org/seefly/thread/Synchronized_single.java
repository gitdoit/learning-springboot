package org.seefly.thread;

/**
 * 该类用来演示单例模式下的懒汉式和同步代码块的联系
 * <p>
 * 问懒汉式和饿汉式有什么不同？
 * 懒汉式的特点是实例的延时加载
 * 那么这样会不会有什么问题？
 * 有，在多线程访问时会出现安全问题
 * 怎么解决？
 * 可以加同步来解决，但是这样会有些低效，可以加双重判断来解决。
 * 加同步的时候，使用的锁是哪一个？
 * 该类所属的字节码文件对象
 */
public class Synchronized_single {

    private static class Single {
        private static Single s = null;

        private Single() {
        }

        public static Single getInstanc() {
            if (s == null) {
                synchronized (Single.class) {
                    if (s == null) {
                        s = new Single();
                    }
                }
            }
            return s;
        }
    }


    private static class Single1 {
        private static final Single1 s = new Single1();

        private Single1() {
        }

        public static Single1 getInstance() {
            return s;
        }
    }


}





