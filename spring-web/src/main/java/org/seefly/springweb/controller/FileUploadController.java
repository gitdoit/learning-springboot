package org.seefly.springweb.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 文件上传接口
 *
 * 应该配套一个HttpClient使用方法
 * 和一个 RestTemplate的文件上传使用方法
 * 还应该有一个RestTemplate的使用大全
 * @author liujianxin
 * @date 2019-06-03 13:29
 */
@RestController
public class FileUploadController {

    @PostMapping(value = "/file")
    public String uploadFile(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String s = bufferedReader.readLine();
        System.out.println(s);
        return "OK";
    }

}
