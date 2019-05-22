package org.seefly.transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.seefly.transaction.model.Student;

/**
 * @author liujianxin
 * @date 2019-05-21 17:21
 */
public interface StudentMapper extends BaseMapper<Student> {
    int insertAndGetKey(Student student);
}
