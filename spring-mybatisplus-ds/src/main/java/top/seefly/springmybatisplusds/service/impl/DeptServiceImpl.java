package top.seefly.springmybatisplusds.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.seefly.springmybatisplusds.entity.DeptEntity;
import top.seefly.springmybatisplusds.mapper.DeptMapper;
import top.seefly.springmybatisplusds.service.DeptService;

/**
 * @author liujianxin
 * @date 2020/3/5 13:40
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, DeptEntity> implements DeptService {

    /**
     * 向数据库1插入一条数据
     */
    @DS("db1")
    @Override
    public void insertDB1(DeptEntity entity){
        save(entity);
    }

    /**
     * 向数据库2插入一条数据
     */
    @DS("db2")
    @Override
    public void insertDB2(DeptEntity entity) {
        save(entity);
    }
}
