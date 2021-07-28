package org.seefly.springmongodb.entity;

import lombok.Data;

/**
 * @author liujianxin
 * @date 2021/7/9 10:12
 */
@Data
public class NestedEntity {
    private String id;
    private String name;
    private Integer age;
    private InnerClass value;
    
    @Data
    public static class InnerClass {
        private String source;
        private Long length;
    }
    
}

