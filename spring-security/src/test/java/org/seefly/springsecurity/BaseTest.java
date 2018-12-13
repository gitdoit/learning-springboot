package org.seefly.springsecurity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {
    @Autowired
    private DataSource dataSource;

    @Test
    public void contextLoads() {
        Class<? extends DataSource> aClass = dataSource.getClass();
        System.out.println(aClass);
    }

}

