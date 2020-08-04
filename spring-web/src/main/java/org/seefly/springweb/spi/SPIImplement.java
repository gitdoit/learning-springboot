package org.seefly.springweb.spi;

/**
 * @author liujianxin
 * @date 2020/8/3 10:32
 */
public class SPIImplement implements SPIInterfaceClazz{

    @Override
    public void doSome() {
        System.out.println("ok let us go");
    }
}
