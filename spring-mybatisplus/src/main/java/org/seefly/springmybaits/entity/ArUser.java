package org.seefly.springmybaits.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.seefly.springmybaits.enums.AgeEnum;
import org.seefly.springmybaits.enums.GenderEnum;
import org.seefly.springmybaits.enums.GradeEnum;

import java.io.Serializable;

/**
 * @author liujianxin
 * @date 2018-12-01 21:30
 */
@Data
@TableName("user")
public class ArUser extends Model<ArUser> {

    private Integer id;

    private Integer roleId;

    private String name;

    private String email;

    /**
     * IEnum接口的枚举处理
     */
    private AgeEnum age;

    /**
     * 原生枚举： 默认使用枚举值顺序： 0：MALE， 1：FEMALE
     */
    private GenderEnum gender;

    /**
     * 原生枚举（带{@link com.baomidou.mybatisplus.annotation.EnumValue}):
     * 数据库的值对应该注解对应的属性
     */
    private GradeEnum grade;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
