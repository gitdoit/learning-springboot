package org.seefly.springmongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author liujianxin
 * @date 2021/9/10 9:29
 */
@Data
@Document
public class Tenant {
    @Id
    private String id;

    private String name;


}
