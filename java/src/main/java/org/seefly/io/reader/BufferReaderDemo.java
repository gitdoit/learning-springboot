package org.seefly.io.reader;

import org.junit.Test;

import java.io.*;
import java.util.stream.Stream;

/**
 * @author liujianxin
 * @date 2019-01-17 16:52
 */
public class BufferReaderDemo {
    /**
     * java8新加的API，读取所有行。还要关闭
     */
    @Test
    public void testReaderLines() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("E:\\test\\io.txt")));
        // 读取一行
        //String line = br.readLine();
        // 读取所有行
        Stream<String> lines = br.lines();
        lines.forEach(System.out::print);
        lines.close();
        br.close();

    }
}
