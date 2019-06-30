package org.seefly.transaction.service;

import org.seefly.transaction.mapper.StudentMapper;
import org.seefly.transaction.model.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Transactional注解中的rollbackFor需要注意
 * 他默认的是回滚运行时异常和ERROR，也就是说方法签名上声明的必检异常若发生时
 * 时不会进行回滚的，文档上写的是这种必检异常是属于业务逻辑，应该由自己维护。
 * 一般也就是一路抛到Controller，确实，程序上要处理这些异常必然会增加逻辑的复杂性，从而
 * 使简单的业务变的复杂，折中的办法也就是根据业务记录异常或者catch重试。
 * @author liujianxin
 * @date 2019-05-22 10:25
 */
@Service
public class StudentService {
    private final StudentMapper studentMapper;

    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    /**
     * 演示Mapper.xml生成主键返回的写法
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Throwable.class)
    public void insertAndGetKey(Student student){
        studentMapper.insertAndGetKey(student);
    }

    /**
     * 演示若调用者有事务，而这个方法没事务的情况
     * 我也不知道会怎么样
     */
    public void insertByNoTransactional(Student student){
        if("s2".equals(student.getName())){
            // 演示其中一个插入失败
            int i = 1 / 0;
        }
        studentMapper.insert(student);
    }

    /**
     * 演示传播行为
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Throwable.class)
    public void insert(Student student){
        studentMapper.insert(student);
    }

    /**
     * 演示传播行为
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW,rollbackFor = Throwable.class)
    public void insertByRequiresNew(Student student){
        if("s2".equals(student.getName())){
            // 演示其中一个插入失败
            int i = 1 / 0;
        }
        studentMapper.insert(student);
    }

    /**
     * 嵌套的事务，若数据库使用Mysql则使用保存点(save point)技术来实现
     * 若子方法发生异常，则只回滚子方法的sql
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.NEVER,rollbackFor = Throwable.class)
    public void insertByNested(Student student){
        studentMapper.insert(student);
    }







}
