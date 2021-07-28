package org.seefly.aop.basic.proxy.interfaces;

/**
 * @author liujianxin
 * @date 2021/4/20 23:14
 */
public class NoInterfaceObj {
    
    public String doSomething(String arg){
        System.out.println("do work:"+arg);
        return "done";
    }
    
}
