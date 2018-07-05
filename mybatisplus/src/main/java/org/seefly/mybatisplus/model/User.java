package org.seefly.mybatisplus.model;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.seefly.mybatisplus.Enum.UserType;

import java.io.Serializable;
import java.util.Date;

@TableName("user")
@Data
public class User extends Model<User> {
    private Integer id;
    private String login_name;
    private String login_pwd;
    private Date create_time;
    private Integer status;
    private Date last_login_time;
    private String salt;
    private UserType user_type;
    private String login_ip;
    private String last_login_ip;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}