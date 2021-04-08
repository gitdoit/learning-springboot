package org.seefly.transaction.service;

import org.seefly.transaction.mapper.StudentMapper;
import org.seefly.transaction.model.Student;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * 演示传播行为
 * @author liujianxin
 * @date 2019-06-24 14:31
 */
@Service
public class BatchStudentService {
    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final ApplicationContext context;

    public BatchStudentService(StudentService studentService, StudentMapper studentMapper, ApplicationContext context) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
        this.context = context;
    }


    /**
     * 演示事务的传播行为.
     * 当前方法和子方法都是使用默认的传播行为REQUIRED，那么循环调用
     * 子方法，则所有调用都会使用同一个事务。
     *
     * 假设批量插入两条数据
     * 插入第一条
     * 1.Creating a new SqlSession
     * 2.Registering transaction synchronization for SqlSession
     * 3.do insert
     * 4.Releasing transactional SqlSession

     * 插入第二条
     * 5.Fetched SqlSession [...] from current transaction
     * 6.do insert
     * 7.Releasing transactional SqlSession
     * 8.Transaction synchronization committing SqlSession
     * 9.Transaction synchronization deregistering SqlSession
     * 10.Transaction synchronization closing SqlSession
     */
    @Transactional(rollbackFor = Throwable.class,isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public void batchInsertByRequired(List<Student> students){
        for(Student student : students){
            studentService.insert(student);
        }
    }


    /**
     * 演示传播行为
     * 当前方法使用REQUIRED，而子方法使用REQUIRES_NEW
     * 那么调用子方法则会创建新的事务，不会使用当前方法的事务。
     *
     * Q:这样的话是不是其中一个事务失败了不会影响之前提交的呢
     * A:是的，第一个提交成功了，写入了数据库。
     */
    @Transactional(rollbackFor = Throwable.class,isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public void batchInsertByRequiresNew(List<Student> students){
        for(Student student : students){
            studentService.insertByRequiresNew(student);
        }
    }

    /**
     * 演示传播行为
     * 当前方法使用REQUIRED，子方法使用NESTED。
     * 若子方法发生异常，则只会滚子方法的sql。
     * 若数据库使用的是Mysql这种支持保存点技术的数据库，那么Spring则利用
     * 保存点实现该功能。所不是，则这种传播行为就和REQUIREDS_NEW类似，也是创建一个新的事务来执行
     * 但不同点是，NESTED会利用当前方法配置的事务隔级别和锁等特性，而REQUIRES_NEW则是可以拥有自己独立的隔离级别和锁特性。
     */
    @Transactional(rollbackFor = Throwable.class,isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public void batchInsertByNested(List<Student> students){
        for(Student student : students){
            studentService.insertByNested(student);
        }
    }


    /**
     * 演示传播行为
     * 当前方法使用REQUIRED，子方法没有事务
     * Q:会发生啥情况？
     * A:这样循环调用子方法相当于这些子方法的执行都在同一个事务里。
     *   若当前方法没有事务，那就是没有事务(废话)。
     *   若当前方法有事务，这就跟REQUIRED一样了
     *
     */
    @Transactional(rollbackFor = Throwable.class,isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public void batchInsertByNoTransactional(List<Student> students){
        for(Student student : students){
            studentService.insertByNoTransactional(student);
        }
    }

    /**
     * 演示事务
     * 当前方法没事务，子方法有事务。
     * Q:如果子方法提交成功，当前方法失败了会怎么样？
     * A:我觉得子方法不会回滚，因为子方法的事务已经提交了。的确是这样！
     */
    public void batchInsertByCurrentNoTransactional(List<Student> students){
        studentMapper.insert(students.get(0));
        int i = 1 / 0;
        studentService.insert(students.get(1));
    }



    /**
     * 演示自掉事务失效的问题
     * 当前方法有事务声明，子方法的声明是NEVER，这种情况
     * 若调用子方法按理说会抛异常，但是没有。
     * 这是应为Spring的事务实现原理是AOP，AOP的原理是动态代理
     * 在自调的过程中是调用类自身的方法，而没有调用代理对象，所以就不会产生AOP。
     * 为了克服这个问题可以装配一个ApplicationContext从容器中拿自身，再掉
     *
     */
    @Transactional(rollbackFor = Throwable.class,isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public void batchInsertByCallSelf(List<Student> students){

        BatchStudentService bean = context.getBean(BatchStudentService.class);
        for(Student student : students){

            // 这种直接调用的方式，子方法的事务声明不会起作用
            //insertBySelf(student);
            
            // 在配置AOP的时候把暴露代理对象打开，然后用这个
            // AopContext.currentProxy();
            
            
            // 可以从容器中获取，使AOP生效
            bean.insertBySelf(student);

        }
    }
    
    
    
    @Transactional(rollbackFor = Throwable.class,isolation = Isolation.READ_COMMITTED,propagation = Propagation.NEVER)
    public void insertBySelf(Student student){
        studentMapper.insert(student);
    }







}
