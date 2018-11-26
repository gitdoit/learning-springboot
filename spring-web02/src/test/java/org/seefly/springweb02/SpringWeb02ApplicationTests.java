package org.seefly.springweb02;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seefly.springweb02.beans.BinderBean;
import org.seefly.springweb02.component.BeanWithCfgPro;
import org.seefly.springweb02.component.BeanWithProperties;
import org.seefly.springweb02.component.BeanWithValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringWeb02ApplicationTests {

    @Autowired
    private BeanWithCfgPro cfgPro;
    @Autowired
    private BeanWithValue value;
    @Autowired
    private BeanWithProperties properties;
    @Autowired
    private BinderBean binderBean;


    @Test
    public void cfgProTest() {
        System.out.println(cfgPro);
    }

    @Test
    public void valueTest(){
        System.out.println(value);
    }

    @Test
    public void testProperties(){
        System.out.println(properties);
    }

    @Test
    public void testBinderBean(){
        System.out.println(binderBean);
    }

}
