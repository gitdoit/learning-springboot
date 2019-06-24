package org.seefly.transaction.service;

import org.junit.Test;
import org.seefly.transaction.BaseTest;
import org.seefly.transaction.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author liujianxin
 * @date 2019-05-22 10:40
 */
public class StudentServiceTest extends BaseTest {
    @Autowired
    private StudentService studentService;
    @Autowired
    private BatchStudentService batchStudentService;

    private List<Student> getList(){
        Student s1 = new Student();
        s1.setName("s1");
        s1.setAge(new Date());
        s1.setSex("男");

        Student s2 = new Student();
        s2.setName("s2");
        s2.setAge(new Date());
        s2.setSex("男");
        return Arrays.asList(s1,s2);
    }

    /**
     * 测试Mapper.xml的生成主键属性
     */
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


    /**
     * 测试当前方法和子方法都是Required
     */
    @Test
    @Commit
    public void testBatch(){
        batchStudentService.batchInsertByRequired(getList());
    }

    /**
     * 测试当前方法是Required子方法是RequiresNew
     * 第一个第一个插入成功，第二个插入失败的现象
     */
    @Test
    @Commit
    public void testRequiredNew(){
        batchStudentService.batchInsertByRequiresNew(getList());
    }

    /**
     * 演示当前方法有事务，子方法无事务的情况
     */
    @Test
    @Commit
    public void testNoTransactional(){
        batchStudentService.batchInsertByNoTransactional(getList());
    }


    /**
     * 演示当前方法没有事务，子方法有事务的情况
     */
    @Test
    @Commit
    public void testCurrentNoTransactional(){
        batchStudentService.batchInsertByCurrentNoTransactional(getList());
    }


    /**
     * 演示自调事务失效的问题和解决办法
     */
    @Test
    public void testBatchInsertBySelf(){
        batchStudentService.batchInsertByCallSelf(getList());
    }


}
