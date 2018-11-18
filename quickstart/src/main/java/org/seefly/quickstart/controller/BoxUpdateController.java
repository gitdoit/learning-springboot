package org.seefly.quickstart.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liujianxin
 * @date 2018-09-13 11:32
 */
@Slf4j
@RestController
public class BoxUpdateController {
    @Value("${file.firm.path}")
    private String firmPath;
    @Value("${file.firm.name}")
    private String firmName;
    @Value("${file.conf.path}")
    private String confPath;
    @Value("${file.conf.name}")
    private String confName;





    @RequestMapping("/downloadFirm")
    public ResponseEntity<byte[]> getFirmware(String box,String firmware) throws IOException {
        log.info("盒子{}固件更新请求，盒子固件版本{}",box,firmware);
        File file = new File(firmPath,firmName);
        if (!file.exists()){
            log.info("没有固件可供下载...");
            return  null;
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentDispositionFormData("attachment",firmName);
        InputStream in = new FileInputStream(file);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        IOUtils.copy(in,os);
        return new ResponseEntity<>(os.toByteArray(),httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping("/downloadConfig")
    public ResponseEntity<byte[]> getConfig(String box,String confVer) throws IOException {
        log.info("盒子{}配置更新请求，盒子配置版本{}",box,confVer);
        File file = new File(confPath,confName);
        if (!file.exists()){
            log.info("没有配置可供下载...");
            return  null;
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentDispositionFormData("attachment",confName);
        InputStream in = new FileInputStream(file);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        IOUtils.copy(in,os);
        return new ResponseEntity<>(os.toByteArray(),httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping("/create")
    public ResponseEntity<byte[]> createConf() throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentDispositionFormData("attachment",confName);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,StandardCharsets.UTF_8));
        bw.write("version=1");
        bw.newLine();
        bw.write("size=10");
        bw.newLine();
        bw.flush();
        return  new ResponseEntity<>(os.toByteArray(),httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping("/uploadConf")
    public void uploadConf(@RequestParam("filedata")MultipartFile filedata,String box) throws IOException {
        FileOutputStream fos = new FileOutputStream("C:\\Users\\liujianxin\\Desktop\\公司项目\\盒子固件\\"+box.replaceAll(":","")+".cvs");
        InputStream in = filedata.getInputStream();
        IOUtils.copy(in,fos);
        fos.close();
        in.close();
        Map<String,String> map = new HashMap<>(2);
        map.put("msg","接收成功");
        map.put("state","success");
    }

}
