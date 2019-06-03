package org.seefly.transaction.application;

import org.junit.Test;
import org.seefly.transaction.BaseTest;
import org.seefly.transaction.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.Date;

/**
 * @author liujianxin
 * @date 2019-05-22 16:03
 */
public class StudentApplicationTest extends BaseTest {
    @Autowired
    private StudentApplication studentApplication;

    @Test
    @Commit
    public void testBatchInsert(){
        Student s1 = new Student();
        s1.setSex("男");
        s1.setAge(new Date());
        s1.setName("A");

        Student s2 = new Student();
        s2.setSex("女");
        s2.setAge(new Date());
        s2.setName("B");

        //studentApplication.batchInsertStudents(Arrays.asList(s1,s2));
        studentApplication.insertAB(Arrays.asList(s1,s2));
    }


}
