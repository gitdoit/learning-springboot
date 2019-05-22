package org.seefly.transaction.service;

import org.junit.Test;
import org.seefly.transaction.BaseTest;
import org.seefly.transaction.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import java.util.Date;

/**
 * @author liujianxin
 * @date 2019-05-22 10:40
 */
public class StudentServiceTest extends BaseTest {
    @Autowired
    private StudentService studentService;

    @Test
    @Commit
    public void testInsertAndGetKey(){
        Student student = new Student();
        student.setName("121");
        student.setAge(new Date());
        student.setSex("男");
        studentService.insertAndGetKey(student);
        System.out.println("主键:"+student.getId());
    }
}
