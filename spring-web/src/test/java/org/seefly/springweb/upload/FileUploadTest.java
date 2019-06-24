package org.seefly.springweb.upload;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
     * 可以使用创建临时文件的方式，将内存数据写到临时文件里，再通过这个临时文件上传到接口
     */
    @Test
    public void testUploadFileByBuffer() throws IOException {
        String data = "内存数据";
        // 就是创建一个临时文件，没有前后缀;临时文件存在这里 -> C:\Users\xiaoming\AppData\Local\Temp\70377211198031427.tmp
        Path tempFile = Files.createTempFile(null, null);
        Files.write(tempFile,data.getBytes());
        File file = tempFile.toFile();

        FileSystemResource resource = new FileSystemResource(file);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", resource);

        String s = restTemplate.postForObject("http://localhost:8080/file", param, String.class);
        // 需要主动删除
        boolean delete = file.delete();

        System.out.println(s+":"+delete);
    }

    /**
     * 能不能直接上传内存流，而不是一个文件
     */
    @Test
    public void testBufferUpload() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("haha".getBytes());
    }

    @Test
    public void testDownload(){
        HttpHeaders header = new HttpHeaders();
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_OCTET_STREAM);
        header.setAccept(list);
        HttpEntity<byte[]> request = new HttpEntity<>(header);
        ResponseEntity<byte[]> forEntity = this.restTemplate.exchange("http://localhost:8080/download", HttpMethod.GET, request, byte[].class);
        String con = new String(forEntity.getBody());
        System.out.println(con);
    }




}
