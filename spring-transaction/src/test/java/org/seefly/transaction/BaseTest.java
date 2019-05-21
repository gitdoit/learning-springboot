package org.seefly.transaction;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seefly.transaction.mapper.StudentMapper;
import org.seefly.transaction.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private StudentMapper mapper;

    @Test
    public void contextLoads() {
        System.out.println(dataSource.getClass());
    }

    @Test
    public void testMapper(){
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("id","01");
        List<Student> student = mapper.selectList(null);
        System.out.println(student);
    }

    @Test
    public void testAr(){
        Student student = new Student();
        student.setId("01");
        Student student1 = student.selectOne(Wrappers.lambdaQuery(student).eq(Student::getId, "01"));
        System.out.println(student1);
    }

}