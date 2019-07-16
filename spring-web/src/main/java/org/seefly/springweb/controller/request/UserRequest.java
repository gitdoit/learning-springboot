package org.seefly.springweb.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author liujianxin
 * @date 2019-07-16 10:33
 */
@AllArgsConstructor
@Data
public class UserRequest {
    private String name;
    private Integer age;
}
