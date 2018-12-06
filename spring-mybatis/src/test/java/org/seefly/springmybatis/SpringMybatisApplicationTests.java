package org.seefly.springmybatis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seefly.springmybatis.dao.Demo;
import org.seefly.springmybatis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringMybatisApplicationTests {
    @Autowired
    private Demo demo;

    @Test
    public void contextLoads() {
        User user = demo.selectById(1);
        System.out.println(user);
    }

}
