package org.seefly.juc;

/**
 * 演示使用ThreadLocal传参
 * 使用该类时应该注意内存泄漏，由于此类和线程绑定，若不主动移除ThreadLocal中设置
 * 的值，那么只有在线程退出时才会被GC回收。若使用了线程池，那线程将会被重复利用，很可能造成内存泄漏
 * 使用{@link ThreadLocal#remove()}方法主动删除资源
 * @author liujianxin
 * @date 2018-11-19 13:31
 */
public class ThreadLocalDemo {

    public static void main(String[] args){
        // 在主线程内设置一个值
        ThreadUtil.setParam("参数");
        // 在主一个线程中取出
        show();
        // 在子线程取不出来
        new Thread(() -> show()).start();
    }

    private static void show(){
        System.out.println(ThreadUtil.getParam());
    }


    private static class ThreadUtil{
        private static ThreadLocal<String> LOCAL = new ThreadLocal<>();

        public static String getParam(){
            return LOCAL.get();
        }

        public static void setParam(String param){
            LOCAL.set(param);
        }
    }
}
