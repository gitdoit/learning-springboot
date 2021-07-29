package org.seefly.springmongodb.entity.aggregate;

import lombok.Data;

import java.util.List;

/**
 * @author liujianxin
 * @date 2021/7/29 18:15
 **/
@Data
public class PersonAggregate {
    private Integer age;
    private List<String> names;

}
