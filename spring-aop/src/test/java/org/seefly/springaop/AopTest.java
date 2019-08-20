package org.seefly.springaop;

import org.junit.Test;
import org.seefly.springaop.component.People;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liujianxin
 * @date 2019-04-22 21:13
 */
public class AopTest extends BaseTest {
    @Autowired
    private People people;


    @Test
    public void testSing(){
        people.sing("冰雨");
    }

    @Test
    public void testDoWork(){
        people.doWork(0);
    }

}
