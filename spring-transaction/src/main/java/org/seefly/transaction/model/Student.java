package org.seefly.transaction.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Student extends Model<Student> {

  /** 学生ID **/
  private String id;
  /** 学生姓名 **/
  private String name;
  /** 年龄 **/
  private Date age;
  /** 性别 **/
  private String sex;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
