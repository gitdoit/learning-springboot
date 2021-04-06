package org.seefly.springannotation.scope;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

/**
 *
 * {@link Scope}注解的使用
 * @author liujianxin
 * @date 2018-12-23 15:04
 */
@Getter
@Setter
public class ScopeBean {
    private String scopeName;
}
