package org.seefly.nio.file;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

/**
 * TODO 完善
 * https://www.cnblogs.com/digdeep/p/4478734.html
 *
 * https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file/326440#326440
 * @author liujianxin
 * @date 2019-01-17 16:25
 */
public class FilesDemo {



    /**
     * 通过Files工具类读取文本文件中所有的行
     * Files.readAllLines底层循环调用BufferedReader.readLine，把所有行放到List中
     * 如果文本文件大小未知，不建议使用
     */
    @Test
    public void testReadAllLines() throws IOException {
        // 更方便的API，但是丢失了每行的换行符，其他的几个方式也是这样啊。之前都没注意到这些问题
        List<String> list = Files.readAllLines(Paths.get("E:\\test\\io.txt"), StandardCharsets.UTF_8);
        list.forEach(System.out::print);
    }

    /**
     * 通过Files获取BufferedReader
     *  bufferedReader.lines()方法返回一个流，若使用变量接受，则记得关闭
     */
    @Test
    public void testFiles() throws IOException {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("E:\\test\\nio.txt"))){
            bufferedReader.lines().forEach(System.out::print);
        }
    }

    /**
     * 通过调用Files.lines底层调用底层调用BufferedReader.lines()方法。
     * 这个方法返回一个流，不像Files.readAllLines一样一次性把所有数据加载到内存中
     * 而是以流的方式，懒加载的读取数据。但是注意需要关闭流
     */
    @Test
    public void testReadLines() throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get("E:\\test\\io.txt"))) {
            stream.forEach(System.out::println);
        }
    }






}
