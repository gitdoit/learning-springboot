package org.seefly.pojo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import java.nio.file.OpenOption;
import java.util.Comparator;
import java.util.Optional;

/**
 * @author liujianxin
 * @date 2018-07-08 16:31
 * 描述信息：
 **/
@Data
public class User implements Comparable{
    private String name;
    private String email;

    public User(String name,String email){
        this.email = email;
        this.name = name;
    }


    @Override
    public int compareTo(Object o) {
        Assert.assertNotNull(o);
        if(o instanceof User){
            User u = (User)o;
            return name.compareTo(u.getName());
        }
        throw new IllegalArgumentException("only User can be used");
    }
}
