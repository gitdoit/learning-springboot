package org.seefly.springbasic.aop;

import org.springframework.stereotype.Component;

/**
 * @author liujianxin
 * @date 2019-04-22 21:01
 */
@Component
public class People {

    public void sing(String songName){
        System.out.println("hello it's me!"+songName);
    }

    public void dance(){
        System.out.println("dancing!");
    }

    public void doWork(int div){
        int i = 1 / div;
    }

}
