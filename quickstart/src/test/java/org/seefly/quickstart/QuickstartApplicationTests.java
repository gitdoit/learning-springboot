package org.seefly.quickstart;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seefly.quickstart.domain.Peo;
import org.seefly.quickstart.domain.PeoPor;
import org.seefly.quickstart.web.IndexController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuickstartApplicationTests {



    @Autowired
    private ApplicationContext ioc;
    @Autowired
    private PeoPor pp;


    @Test
    public void printa(){
        System.out.println(pp);
    }



    @Test
    public void contextLoads() {
        boolean b = ioc.containsBean("helloService");
        System.out.println(b);
    }

}
