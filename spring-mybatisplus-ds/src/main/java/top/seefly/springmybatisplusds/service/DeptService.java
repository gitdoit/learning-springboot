package top.seefly.springmybatisplusds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.seefly.springmybatisplusds.entity.DeptEntity;

public interface DeptService extends IService<DeptEntity> {

     void insertDB1(DeptEntity entity);

     void insertDB2(DeptEntity entity);

}
