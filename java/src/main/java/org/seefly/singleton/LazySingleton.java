package org.seefly.singleton;

/**
 * 懒加载的单例模式
 * 在getInstance上加锁防止并发环境下创建多个实例，并进行懒加载
 * 优点是可以控制单例的创建时间，缺点是在高并发环境下性能较差。
 *
 * @author liujianxin
 * @date 2018-11-20 19:56
 */
public class LazySingleton {
    private static LazySingleton singleton = null;
    private LazySingleton(){}

    public static synchronized LazySingleton getInstance(){
        if(singleton == null){
            singleton = new LazySingleton();
        }
        return singleton;
    }
}
