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
public class Score extends Model<Score> {

  /** 学生ID **/
  private String sid;
  /** 课程ID **/
  private String cid;
  /** 成绩 **/
  private Double score;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
