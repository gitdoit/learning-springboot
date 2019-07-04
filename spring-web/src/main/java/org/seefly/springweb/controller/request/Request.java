package org.seefly.springweb.controller.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;

/**
 * @author liujianxin
 * @date 2019-07-01 17:59
 */
@Data
public class Request {
    private String string;
    private Integer integer;
    private String[] array;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    private Map<String,String> map;
}
