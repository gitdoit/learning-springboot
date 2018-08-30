package org.seefly.mybatisplus.model;


import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.seefly.mybatisplus.Enum.UserType;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author liujianxin
 * @date 2018年7月16日
 */
@TableName("user")
@Data
public class User extends Model<User> {
    private Integer id;
    @TableLogic
    private String login_name;
    private String login_pwd;
    private Timestamp create_time;
    private Integer status;
    private Timestamp last_login_time;
    private String salt;
    private UserType user_type;
    private String login_ip;
    private String last_login_ip;
    public String[] haha = null;

    public User(){}


    public User(Integer id, String login_name, String login_pwd, Timestamp create_time, Integer status, Timestamp last_login_time, String salt, Integer user_type, String login_ip, String last_login_ip) {
        this.id = id;
        this.login_name = login_name;
        this.login_pwd = login_pwd;
        this.create_time = create_time;
        this.status = status;
        this.last_login_time = last_login_time;
        this.salt = salt;
        this.user_type = UserType.getEnumByType(user_type);
        this.login_ip = login_ip;
        this.last_login_ip = last_login_ip;
    }

    public User(Integer id,String login_name){
        this.id = id;
        this.login_name = login_name;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}