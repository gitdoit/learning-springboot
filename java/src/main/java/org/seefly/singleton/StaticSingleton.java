package org.seefly.singleton;

/**
 * @author liujianxin
 * @date 2018-11-21 09:26
 */
public class StaticSingleton {
    private StaticSingleton(){}

    private static class SingletonHolder{
        private static StaticSingleton instance = new StaticSingleton();
    }

    public static StaticSingleton getInstance(){
        return SingletonHolder.instance;
    }
}
