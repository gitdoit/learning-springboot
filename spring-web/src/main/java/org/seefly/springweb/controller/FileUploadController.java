package org.seefly.springweb.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 文件上传接口
 *
 * 应该配套一个HttpClient使用方法
 * 和一个 RestTemplate的文件上传使用方法
 * 还应该有一个RestTemplate的使用大全
 * 测试类单元测试下面  FileUploadTest
 * TODO 但是我现在懒得做
 *
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


    @GetMapping("/download")
    public void getFirmware(HttpServletResponse response, String box) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=yeye.txt");
        try(FileInputStream in = new FileInputStream("C:\\Users\\hkdw232\\Desktop\\data\\yeye.txt");
            ServletOutputStream os = response.getOutputStream()){
            IOUtils.copy(in, os);
            os.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
