package org.seefly.singleton;

/**
 * 简单的单例模式
 *
 * 这种单例模式实现简单，代码易读，性能良好。
 * 但是唯一的缺点是不能控制实例的创建时间。
 * @author liujianxin
 * @date 2018-11-20 19:51
 */
public class SimpleSingleton {
    /**
     * 避免被直接引用
     */
    private static SimpleSingleton instance = new SimpleSingleton();

    /**
     * 单例模式只能有一个实例。构造方法不能被外部访问！
     */
    private SimpleSingleton(){}

    /**
     * 通过静态方法获取单例。
     * @return 单例
     */
    public static SimpleSingleton getInstance(){
        return instance;
    }
}
