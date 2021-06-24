package org.seefly.mytomcat.controller;

import net.sf.json.JSONObject;
import org.seefly.mytomcat.model.CaAuthBody;
import org.seefly.mytomcat.model.SOAServerAuthBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liujianxin
 * @date 2021/5/26 9:02
 */
@RestController
@RequestMapping("/ca")
@CrossOrigin
public class CaController {
    
    
    @PostMapping("/verify")
    public String verify(@RequestBody CaAuthBody body) {
        System.out.println(body);
        String strSOAPServer = "http://60.167.89.95:8881/svsapi/api/verifyAuthP1";
        SOAServerAuthBody parse = body.parse("1234567");
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(strSOAPServer, parse, String.class);
        System.out.println(response);
        return "ok";
    }
    
//    @PostMapping("/login")
//    public String test(HttpServletRequest request){
//        Map<String, String> map = new HashMap();
//        Map<String, String> map2 = new HashMap();
//        map.put("signData", captcha);
//        map.put("strCert", host);
//        map.put("strData", data);
//        map2.put("data", JSONObject.fromObject(map).toString());
//        String postdata = JSONObject.fromObject(map2).toString();
//        HttpPost httpPost = new HttpPost(strSOAPServer);
//        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
//        StringEntity se = new StringEntity(postdata, "utf-8");
//        se.setContentType("text/json");
//        se.setContentEncoding(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
//        httpPost.setEntity(se);
//        HttpClient httpClient = HttpClientBuilder.create().build();
//        HttpResponse httpResponse = null;
//
//
//        return null;
//    }


}
