package org.seefly.springmybaits.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.seefly.springmybaits.enums.AgeEnum;
import org.seefly.springmybaits.enums.GenderEnum;
import org.seefly.springmybaits.enums.GradeEnum;

/**
 * <p>
 * 用户实体对应表 user
 * </p>
 *
 * @author hubin
 * @since 2018-08-11
 */
@Data
@Accessors(chain = true)
public class User extends BaseEntity {

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

}
