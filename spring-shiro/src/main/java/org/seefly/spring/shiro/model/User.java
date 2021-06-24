package org.seefly.spring.shiro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author liujianxin
 * @date 2021/6/8 17:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    private String id;
    @NotBlank(message = "用户名不能为空！")
    private String userName;
    @NotBlank(message = "密码不能为空！")
    private String password;
    /**
     * 用户对应的角色集合
     */
    private Set<Role> roles;

}
