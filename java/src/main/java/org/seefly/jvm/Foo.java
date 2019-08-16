package org.seefly.jvm;

import sun.reflect.Reflection;

/**
 * @author liujianxin
 * @date 2019-08-15 17:14
 */
public class Foo {


    public void show(){
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Foo.class.getName())&& ste.getClassName().indexOf("java.lang.Thread")!=0) {
                if (callerClassName==null) {
                    callerClassName = ste.getClassName();
                } else if (!callerClassName.equals(ste.getClassName())) {
                    System.out.println(ste.getClassName());
                    break;
                }
            }
        }
    }


    public void showByNa(){
        Class<?> caller = Reflection.getCallerClass();
        System.out.println(caller.getName());
    }
}
