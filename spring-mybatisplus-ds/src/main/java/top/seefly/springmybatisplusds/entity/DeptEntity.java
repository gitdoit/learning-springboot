package top.seefly.springmybatisplusds.entity;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * @author liujianxin
 * @date 2020/3/5 11:27
 */
@Data
@TableName("dept")
public class DeptEntity extends Model<DeptEntity> {
    @TableId(type = IdType.AUTO)
    private int deptno;
    private String dname;
    private String dbSource;

    @DS("db1")
    public DeptEntity selectFromDB1(String dname) {
        QueryWrapper<DeptEntity> qw = new QueryWrapper<>();
        return this.selectOne(qw);
    }

}


