package org.seefly.springmybaits;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seefly.springmybaits.entity.ArUser;
import org.seefly.springmybaits.entity.User;
import org.seefly.springmybaits.enums.AgeEnum;
import org.seefly.springmybaits.enums.GenderEnum;
import org.seefly.springmybaits.enums.GradeEnum;
import org.seefly.springmybaits.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringMybaitsApplicationTests {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads() {
        if(dataSource instanceof  DruidDataSource){
            DruidDataSource druidDataSource = (DruidDataSource)dataSource;
            System.out.println(druidDataSource);
        }
        System.out.println(dataSource);
    }

    @Test
    public void testInsert(){
        User user = new User();
        user.setName("test");
        user.setAge(AgeEnum.ONE);
        user.setGrade(GradeEnum.HIGH);
        user.setGender(GenderEnum.MALE);
        user.setEmail("123@123.com");
        System.out.println(userMapper.insert(user));
    }

    @Test
    public void testArInsert(){
        ArUser user = new ArUser();
        user.setName("test");
        user.setAge(AgeEnum.ONE);
        user.setGrade(GradeEnum.HIGH);
        user.setGender(GenderEnum.MALE);
        user.setEmail("123@123.com");
        System.out.println(user.insert());
    }

}
