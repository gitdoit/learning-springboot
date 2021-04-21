package org.seefly.springaop.proxy.util;

import sun.misc.ProxyGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author liujianxin
 * @date 2019/8/29 22:45
 */
public class GenerateClassUtil {
    
    public static void generateClassFile(String path,String proxyName, Class clazz) throws IOException {
        byte[] bytes = ProxyGenerator.generateProxyClass(proxyName, new Class[] {clazz});
        Files.write(Paths.get(path + proxyName + ".class"), bytes, StandardOpenOption.CREATE);
    }
}
