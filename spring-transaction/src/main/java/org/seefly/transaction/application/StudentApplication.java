package org.seefly.transaction.application;

import org.seefly.transaction.model.Student;
import org.seefly.transaction.service.StudentService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author liujianxin
 * @date 2019-05-22 15:56
 */
@Component
public class StudentApplication {
    private final StudentService studentService;

    public StudentApplication(StudentService studentService) {
        this.studentService = studentService;
    }


    //@Transactional(rollbackFor = Throwable.class)
    public void batchInsertStudents(List<Student> students){
        if(!CollectionUtils.isEmpty(students)){
            for(Student student : students){
                student.insert();
            }
        }
    }
}
