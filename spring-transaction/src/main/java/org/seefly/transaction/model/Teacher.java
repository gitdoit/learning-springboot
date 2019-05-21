package org.seefly.transaction.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Teacher extends Model<Teacher> {

  /** 教师ID **/
  private String id;
  /** 教师姓名 **/
  private String name;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
