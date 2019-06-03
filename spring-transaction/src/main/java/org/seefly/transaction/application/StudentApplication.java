package org.seefly.transaction.application;

import org.seefly.transaction.model.Student;
import org.seefly.transaction.service.StudentService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author liujianxin
 * @date 2019-05-22 15:56
 */
@Component
@Transactional(rollbackFor = Throwable.class)
public class StudentApplication {
    private final StudentService studentService;

    public StudentApplication(StudentService studentService) {
        this.studentService = studentService;
    }


    public void batchInsertStudents(List<Student> students){
        if(!CollectionUtils.isEmpty(students)){
            for(Student student : students){
                student.insert();
            }
        }
    }

    public void insertAB(List<Student> students){
        insertA(students.get(0));
        insertB(students.get(1));
    }

    public void insertA(Student student){
        studentService.insertAndGetKey(student);
        System.out.println("A:"+student.getId());
    }

    public void insertB(Student student){
        studentService.insertAndGetKey(student);
        System.out.println("B"+student.getId());
        int i = 1 / 0;
    }


}
