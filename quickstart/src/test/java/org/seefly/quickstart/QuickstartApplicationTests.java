package org.seefly.quickstart;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seefly.quickstart.domain.PeoPor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class QuickstartApplicationTests {



    @Autowired
    private ApplicationContext ioc;
    @Autowired
    private PeoPor pp;

    @Test
    public void printa(){
        System.out.println(pp);
    }

    /**
     * 测试lombok以及编写log配置文件
     */
    @Test
    public void testLog(){
        log.trace("这个框架用起来，很舒适");
        log.debug("这个debug用起来，很舒适");
        log.info("这个info用起来很舒适");
        log.warn("这个warn用起来，很舒适");
        log.error("这个error用起来，很舒适");
    }



    @Test
    public void contextLoads() {
        boolean b = ioc.containsBean("helloService");
        System.out.println(b);
    }

}
