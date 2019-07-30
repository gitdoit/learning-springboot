package org.seefly.thread;

/**
 * @author liujianxin
 * @date 2019-07-30 11:11
 */
public class ThreadUtils {


    public static Thread newAndSleep(int time){
        Thread thread = new Thread(()->{
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        return thread;
    }

    public static Thread newAndLoop(){
        Thread thread = new Thread(()->{
            while (true){
            }
        });
        thread.start();
        return thread;
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
