package org.seefly.jvm;

import org.junit.Test;

/**
 * @author liujianxin
 * @date 2019-08-15 17:17
 */
public class MyTest {

    public static void main(String[] args) {
        Foo foo = new Foo();
        foo.show();
    }

    @Test
    public void test(){

    }

    @Test
    public void testB(){
        Foo foo = new Foo();
        foo.showByNa();
    }
}
