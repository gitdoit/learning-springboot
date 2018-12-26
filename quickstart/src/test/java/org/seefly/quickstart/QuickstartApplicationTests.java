package org.seefly.quickstart;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seefly.quickstart.domain.PeoPor;
import org.seefly.quickstart.model.MacInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@Slf4j
public class QuickstartApplicationTests {



    @Autowired
    private ApplicationContext ioc;
    @Autowired
    private PeoPor pp;




    @Test
    public void testForHTTP() throws Exception {
        MacInfo macInfo = new MacInfo();
        List<MacInfo> list = macInfo.selectAll();
        for(int i = 0 ; i < 10 ; i ++){
            String mac = list.get(i).getSource();
            //getResult(getMac(mac));
        }

    }

    public String  getMac(String mac){
        mac = mac.toUpperCase();
        String[] strings = mac.split("");
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<strings.length;i++) {
            if (i % 2 == 0) {
                sb.append(strings[i]);

            } else {
                sb.append(strings[i]).append(":");
            }
        }
        return  StringUtils.removeEnd(sb.toString(), ":");
    }

    @Test
    public void getResult() throws Exception {



        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost post = new HttpPost();
        post.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.setURI(new URI("127.0.0.1/parser/rsa/encode"));

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("phone", "13665581111"));
        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        CloseableHttpResponse response = client.execute(post);

        if(response.getStatusLine().getStatusCode() == 200){

            /*Optional<Cookie> any = cookieStore.getCookies().stream().filter(cookie -> "token".equals(cookie.getName())).distinct().findAny();
            Cookie cookie = any.get();
            System.out.println(cookie.getValue());*/

            InputStream content = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(content,"UTF-8"));
            String line;
            while ((line = br.readLine()) != null){
                System.out.println(line);
            }
            System.out.println("===========================");
        }
    }





    @Test
    public void printa(){
        RestTemplate restTemplate = new RestTemplate();
        /*HttpHeaders httpHeaders = new HttpHeaders();
        MultiValueMap<String, String> body= new LinkedMultiValueMap();
        body.add("phone","13665581111");
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<>(body,httpHeaders);



        //String map = restTemplate.exchange("http://127.0.0.1/parser/rsa/encode",httpEntity,String.class);
        ResponseEntity<String> exchange = restTemplate.exchange("http://127.0.0.1/parser/rsa/encode", HttpMethod.POST, httpEntity, String.class);
        String body1 = exchange.getBody();
        System.out.println(body1);*/
        Map<String,String> s = restTemplate.postForObject("http://127.0.0.1/parser/rsa/batch-encode", Arrays.asList("sdfdsf","sdfsdf"), Map.class);
        System.out.println();
        System.out.println();
    }

    /**
     * 测试lombok以及编写log配置文件
     */
    @Test
    public void testLog(){
        log.trace("这个框架用起来，很舒适");
        log.debug("这个debug用起来，很舒适");
        log.info("这个info用起来很舒适");
        log.warn("这个warn用起来，很舒适");
        log.error("这个error用起来，很舒适");
    }



    @Test
    public void contextLoads() {
        boolean b = ioc.containsBean("helloService");
        System.out.println(b);
    }

}
