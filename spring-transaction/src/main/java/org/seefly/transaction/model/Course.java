package org.seefly.transaction.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Alias("course")
public class Course extends Model<Course> {

  /** 课程ID **/
  private String cid;
  /** 课程名称 **/
  private String cname;
  /** 授课教师ID **/
  private String tid;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
