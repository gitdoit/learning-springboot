package org.seefly.code;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author liujianxin
 * @date 2019-02-25 17:19
 */
public class Base64Demo {
    @Test
    public void enCode() {
        Base64.Decoder decoder = Base64.getDecoder();
        Base64.Encoder encoder = Base64.getEncoder();

        String s = encoder.encodeToString("nihao".getBytes(StandardCharsets.UTF_8));
        System.out.println(s);
        System.out.println(new String(decoder.decode(s),StandardCharsets.UTF_8));
    }
}
