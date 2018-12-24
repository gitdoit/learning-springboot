package org.seefly.springannotation.entity;

import lombok.Data;
import org.springframework.context.annotation.Import;

/**
 *
 * 演示使用{@link Import}注解，导入这个bean到容器
 * @author liujianxin
 * @date 2018-12-23 16:15
 */
@Data
public class ImportBean {
    private String name;

    public ImportBean(){
        System.out.println("创建了一个ImportBean....");
    }
}
