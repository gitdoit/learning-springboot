package org.seefly.springwebsocket.model;

import java.security.Principal;

/**
 * @author liujianxin
 * @date 2019-02-26 15:10
 */
public class MyUser implements Principal {
    private String uuid;

    public MyUser(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getName() {
        return uuid;
    }
}
