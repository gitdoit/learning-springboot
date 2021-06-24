package org.seefly.spring.shiro.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * @author liujianxin
 * @date 2021/6/8 17:26
 */
@Data
@AllArgsConstructor
public class Role {
    
    private String id;
    private String roleName;
    /**
     * 角色对应权限集合
     */
    private Set<Permissions> permissions;

}
