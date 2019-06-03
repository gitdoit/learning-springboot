package org.seefly.springweb.upload;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author liujianxin
 * @date 2019-06-03 13:59
 */
public class FileUploadTest {
    private RestTemplate restTemplate = new RestTemplate();


    /**
     * 基本的从本地文件读取数据通过RestTemplate上传
     */
    @Test
    public void testUploadFile(){
        FileSystemResource resource = new FileSystemResource(new File("C:\\Users\\liujianxin\\Desktop\\新建文本文档.txt"));
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);
        String s = restTemplate.postForObject("http://localhost:8080/file", param, String.class);
        System.out.println(s);
    }


    /**
     * 但现在我的数据在内存中，没有对应的磁盘文件
     * 之前的从磁盘读取文件的方式行不通了，如果要这样做 就必须把内存中的数据写到磁盘上，再从磁盘读到内存，这就是脱裤子放屁，多此一举
     *
     * 有没有这样一种方法，把内存数据直接通过RestTemplate传出去？
     */
    @Test
    public void testUploadFileByBuffer() throws IOException {
        String data = "内存数据";
        Path tempFile = Files.createTempFile(null, null);
        Files.write(tempFile,data.getBytes());
        File file = tempFile.toFile();

        FileSystemResource resource = new FileSystemResource(file);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);

        String s = restTemplate.postForObject("http://localhost:8080/file", param, String.class);
        System.out.println(s);

    }
}
