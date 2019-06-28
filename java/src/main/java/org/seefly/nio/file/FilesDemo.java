package org.seefly.nio.file;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;
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
        //List<String> list = Files.readAllLines(Paths.get("E:\\test\\io.txt"), StandardCharsets.UTF_8);
        //list.forEach(System.out::print);
        Files.write(Paths.get("E:\\test\\io12.txt"),"haha中".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
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

    /**
     * 删除文件
     */
    @Test
    public void testDelete() throws IOException {
        // 若文件/文件夹 存在则删除。若为文件夹则文件夹不能非空
        Files.deleteIfExists(Paths.get("E:\\test\\io.txt"));
        // 同上，看名字
        Files.delete(Paths.get("E:\\test\\io.txt"));


    }

    /**
     * 万一能用到呢？
     * 可以用来迭代删除一个未知深度的文件夹，window上一个delete的事情
     *
     * walk方法说它将给定路径作为根，进行深度优先遍历
     * 跳过被安全管理器保护的文件，默认情况下也不会遍历文件链接。
     * 也是弱引用的(可能是在遍历生成流的过程中某个文件被更新了，这里是察觉不到的)
     * 可以指定最大深度，若为Integer.MAX，则无限深度。
     * 流中元素为Path
     *
     * 若指定参数{@link FileVisitOption#FOLLOW_LINKS}，那么将会遍历符号连接，若符号连接到跟路径，则会抛出一个死循环的异常。
     */
    @Test
    public void testIterate() throws IOException {
        // 从根到叶开始遍历打印文件路径
        Files.walk(Paths.get("F:\\LOG")).map(Path::toFile).forEach(System.out::println);

        // 迭代删除，需要从叶到根开始删除，所以要对流中的元素进行反序排列
        //Files.walk(Paths.get("E:\\test\\io.txt")).sorted(Comparator.reverseOrder()).map(Path::toFile) .peek(System.out::println).forEach(File::delete);
    }

    /**
     * 简化的查找指定的文件
     */
    @Test
    public void testFind() throws IOException {
        Stream<Path> pathStream = Files.find(Paths.get("F:\\LOG"), 10, (a, b) -> {
            String name = a.toFile().getName();
            if(b instanceof DosFileAttributes){
                DosFileAttributes attr = (DosFileAttributes)b;
            }
            return name.startsWith("c");
        });
        pathStream.forEach(System.out::println);
    }







}
