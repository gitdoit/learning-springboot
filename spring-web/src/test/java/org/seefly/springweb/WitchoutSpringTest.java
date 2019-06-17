package org.seefly.springweb;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author liujianxin
 * @date 2019-06-03 17:21
 */
public class WitchoutSpringTest {
    @Test
    public void test(){
        List<String> strings = Arrays.asList("1", "2");
        System.out.println(String.join("\r\n",strings));
    }
}
