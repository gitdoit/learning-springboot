package org.seefly.nio.file;

import org.junit.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 这玩意用来干啥？？
 * @author liujianxin
 * @date 2019-01-08 19:27
 */
public class PathDemo {
    /**
     * How to get a Path
     */
    public void buildPath(){
        // 使用绝对路径
        Path path = Paths.get("E://test/nio.txt");
        // 使用相对路径
        Path path1 = Paths.get("/resource/test.text");
        // other way
        Path path2 = FileSystems.getDefault().getPath("E://test/nio.txt");
    }

    /**
     * File - Path
     */
    public void transfer(){
        File file = new File("E://test/nio.txt");
        // in this way
        Path path = file.toPath();
        // back
        path.toFile();
        // to URI
        file.toURI();
    }

    @Test
    public void getPathInfo(){
        Path path = Paths.get("E://test//nio.txt");
        System.out.println("文件名："+path.getFileName());
        // 深度
        System.out.println("名称元素的数量："+path.getNameCount());
        System.out.println("是否以指定的路径开头:"+path.startsWith("E:\\"));
        System.out.println("该路径的字符串形式:"+path.toFile());
    }


}
