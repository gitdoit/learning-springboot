package org.seefly.transaction.service;

import org.seefly.transaction.mapper.StudentMapper;
import org.seefly.transaction.model.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author liujianxin
 * @date 2019-05-22 10:25
 */
@Service
public class StudentService {
    private final StudentMapper studentMapper;

    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Throwable.class)
    public void insertAndGetKey(Student student){
        studentMapper.insertAndGetKey(student);
    }
}
