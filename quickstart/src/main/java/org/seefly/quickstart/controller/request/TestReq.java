package org.seefly.quickstart.controller.request;

import lombok.Data;

import java.util.List;

/**
 * @author liujianxin
 * @date 2018-07-01 01:14
 * 描述信息：
 **/
@Data
public class TestReq {
    private List<Integer> id;
    private String str;
}
