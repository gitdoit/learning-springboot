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
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
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
            getResult(getMac(mac));
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

    public void getResult(String mac) throws Exception {



        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost post = new HttpPost();
        post.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.setURI(new URI("http://baidu.com"));

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("token", ""));
        nvps.add(new BasicNameValuePair("macs", ""));
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
        System.out.println(pp);
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
