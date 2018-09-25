package org.seefly.quickstart;

import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author liujianxin
 * @date 2018-09-14 13:47
 */
public class TestMD5 {


    @Test
    public void testGet() throws IOException {
        String s = DigestUtils.md5DigestAsHex(new FileInputStream("C:\\Users\\liujianxin\\Desktop\\公司项目\\盒子固件\\qlnk4107-qcfg_1_131_702_workren_upgrade.bin"));
        System.out.println(s);
    }
}
