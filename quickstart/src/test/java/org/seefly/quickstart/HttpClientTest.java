package org.seefly.quickstart;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author liujianxin
 * @date 2018-07-10 14:24
 * 描述信息：
 **/
public class HttpClientTest {

    @Test
    public void getToken() throws Exception {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost post = new HttpPost();
        post.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.setHeader("platform", "PC");
        post.setURI(new URI("http://geeker.worken.test.cn/user-apis/login.action"));

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("loginName", "shuaige"));
        nvps.add(new BasicNameValuePair("loginPwd", "111111"));
        nvps.add(new BasicNameValuePair("type", "0"));
        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        CloseableHttpResponse response = client.execute(post);

        if(response.getStatusLine().getStatusCode() == 200){

            Optional<Cookie> any = cookieStore.getCookies().stream().filter(cookie -> "token".equals(cookie.getName())).distinct().findAny();
            Cookie cookie = any.get();
            System.out.println(cookie.getValue());

            /*InputStream content = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(content,"UTF-8"));
            String line;
            while ((line = br.readLine()) != null){
                System.out.println(line);
            }*/
        }
    }
}
