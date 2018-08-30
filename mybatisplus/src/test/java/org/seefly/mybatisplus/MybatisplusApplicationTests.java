package org.seefly.mybatisplus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seefly.mybatisplus.Enum.UserType;
import org.seefly.mybatisplus.mapper.UserMapper;
import org.seefly.mybatisplus.model.MacInfo;
import org.seefly.mybatisplus.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisplusApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void contextLoads() {
        System.out.println(userMapper.selectOne("222","222"));
    }

    @Test
    public void testUpdate(){
       /* MacInfo mc = new MacInfo();
        mc.setDeviceMac("sdfsdf");
        mc.setSourceMac("sdfsf:sdfsf");
        mc.setTargetMac("sdfsdf:sdfsdf:aaaa");
        mc.insert();*/
       User user = new User(333,"sdfsdfsdfsfd");
    }

}
