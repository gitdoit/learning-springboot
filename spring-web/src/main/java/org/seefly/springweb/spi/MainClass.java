package org.seefly.springweb.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author liujianxin
 * @date 2020/8/3 10:33
 */
public class MainClass {
    
    public static void main(String[] args) {
        ServiceLoader<SPIInterfaceClazz> load = ServiceLoader.load(SPIInterfaceClazz.class);
        Iterator<SPIInterfaceClazz> iterator = load.iterator();
        while (iterator.hasNext()) {
            SPIInterfaceClazz next = iterator.next();
            next.doSome();
        }
    }
}
