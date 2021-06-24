package org.seefly.springweb;

import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liujianxin
 * @date 2019-06-03 17:21
 */
public class CaTest {
    
    private static String strCert = "-----BEGIN CERTIFICATE-----\n"
            + "MIIECDCCA62gAwIBAgIIaeMA/AAnqfQwDAYIKoEcz1UBg3UFADB2MQswCQYDVQQG\n"
            + "EwJDTjEOMAwGA1UECAwFQW5IdWkxDjAMBgNVBAcMBUhlRmVpMSYwJAYDVQQKDB1B\n"
            + "bkh1aSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTENMAsGA1UECwwEQUhDQTEQMA4G\n"
            + "A1UEAwwHQUhDQVNNMjAeFw0yMTA0MTgxNjAwMDBaFw0yMjA0MTkxNTU5NTlaMHcx\n"
            + "CzAJBgNVBAYTAkNOMRUwEwYDVQQKDAwyMDIxMDQxOTEwMDExCzAJBgNVBAsMAjk5\n"
            + "MSEwHwYDVQQNDBjmtYvor5Xns7vnu5/mraPlvI/moLkjOTkxITAfBgNVBAMMGOiC\n"
            + "peilv+WOv+aVsOaNruWxgOa1i+ivlTBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IA\n"
            + "BCA8g9TY6PhfMTh1ip5Lj86tH7HkIK8sDJ+8QM/EBiDV2zvLL1tBQplQxslG2f0W\n"
            + "F5hCgOesdb8Qqx7Wq6OvHp+jggIgMIICHDAMBgNVHRMEBTADAQEAMB0GA1UdJQQW\n"
            + "MBQGCCsGAQUFBwMCBggrBgEFBQcDBDALBgNVHQ8EBAMCAMAwHwYDVR0jBBgwFoAU\n"
            + "Rpm8YWLiulOpDIjSzV2WwMgwus8wgcoGA1UdHwSBwjCBvzCBvKCBuaCBtoaBjmxk\n"
            + "YXA6Ly9sZGFwLmFoZWNhLmNuOjM4OS9DTj1BSENBU00yLENOPUFIQ0FTTTIsIE9V\n"
            + "PUNSTERpc3RyaWJ1dGVQb2ludHMsIG89YWhjYT9jZXJ0aWZpY2F0ZVJldm9jYXRp\n"
            + "b25MaXN0P2Jhc2U/b2JqZWN0Y2xhc3M9Y1JMRGlzdHJpYnV0aW9uUG9pbnSGI2h0\n"
            + "dHA6Ly93d3cuYWhlY2EuY24vY3JsL0FIQ0FTTTIuY3JsMIHSBggrBgEFBQcBAQSB\n"
            + "xTCBwjCBiwYIKwYBBQUHMAKGf2xkYXA6Ly9sZGFwLmFoZWNhLmNuOjM4OS9DTj1B\n"
            + "SENBU00yLENOPUFIQ0FTTTIsIE9VPWNBQ2VydGlmaWNhdGVzLCBvPWFoY2E/Y0FD\n"
            + "ZXJ0aWZpY2F0ZT9iYXNlP29iamVjdENsYXNzPWNlcnRpZmljYXRpb25BdXRob3Jp\n"
            + "dHkwMgYIKwYBBQUHMAKGJmh0dHA6Ly93d3cuYWhlY2EuY24vY2FjZXJ0L0FIQ0FT\n"
            + "TTIuY2VyMB0GA1UdDgQWBBRg/GZwA9KBr0OL0arH15VX5asGdDAMBggqgRzPVQGD\n"
            + "dQUAA0cAMEQCIHzCgPNXaZHRKZNUjAYzFTe/uE6STI+M66o45MSMv5iEAiAQtndc\n"
            + "GOm77rCAQf8UpT6gJzOs7rp1B1pVsvwDM4tVtQ==\n" + "-----END CERTIFICATE-----\n";
    private static String signData = "MEQCION/2uwfMEsg/82ug80jpMrBVc92Qh19C+KK72HP2I42AiBJbKb3exdeDqDET16//hbE9alJpgJFPILzf1AwdgAQCQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAC0tLS0tQkVHSU4gQ0VSVElGSUNBVEUtLS0tLQpNSUlFQ0RDQ0E2MmdBd0lCQWdJSWFlTUEvQUFucWZRd0RBWUlLb0VjejFVQmczVUZBREIyTVFzd0NRWURWUVFHCkV3SkRUakVPTUF3R0ExVUVDQXdGUVc1SWRXa3hEakFNQmdOVkJBY01CVWhsUm1WcE1TWXdKQVlEVlFRS0RCMUIKYmtoMWFTQkRaWEowYVdacFkyRjBhVzl1SUVGMWRHaHZjbWwwZVRFTk1Bc0dBMVVFQ3d3RVFVaERRVEVRTUE0RwpBMVVFQXd3SFFVaERRVk5OTWpBZUZ3MHlNVEEwTVRneE5qQXdNREJhRncweU1qQTBNVGt4TlRVNU5UbGFNSGN4CkN6QUpCZ05WQkFZVEFrTk9NUlV3RXdZRFZRUUtEQXd5TURJeE1EUXhPVEV3TURFeEN6QUpCZ05WQkFzTUFqazUKTVNFd0h3WURWUVFOREJqbXRZdm9yNVhuczd2bnU1L21yYVBsdkkvbW9Ma2pPVGt4SVRBZkJnTlZCQU1NR09pQwpwZWlsditXT3YrYVZzT2FOcnVXeGdPYTFpK2l2bFRCWk1CTUdCeXFHU000OUFnRUdDQ3FCSE05VkFZSXRBMElBCkJDQThnOVRZNlBoZk1UaDFpcDVMajg2dEg3SGtJSzhzREorOFFNL0VCaURWMnp2TEwxdEJRcGxReHNsRzJmMFcKRjVoQ2dPZXNkYjhRcXg3V3E2T3ZIcCtqZ2dJZ01JSUNIREFNQmdOVkhSTUVCVEFEQVFFQU1CMEdBMVVkSlFRVwpNQlFHQ0NzR0FRVUZCd01DQmdnckJnRUZCUWNEQkRBTEJnTlZIUThFQkFNQ0FNQXdId1lEVlIwakJCZ3dGb0FVClJwbThZV0xpdWxPcERJalN6VjJXd01nd3VzOHdnY29HQTFVZEh3U0J3akNCdnpDQnZLQ0J1YUNCdG9hQmpteGsKWVhBNkx5OXNaR0Z3TG1Gb1pXTmhMbU51T2pNNE9TOURUajFCU0VOQlUwMHlMRU5PUFVGSVEwRlRUVElzSUU5VgpQVU5TVEVScGMzUnlhV0oxZEdWUWIybHVkSE1zSUc4OVlXaGpZVDlqWlhKMGFXWnBZMkYwWlZKbGRtOWpZWFJwCmIyNU1hWE4wUDJKaGMyVS9iMkpxWldOMFkyeGhjM005WTFKTVJHbHpkSEpwWW5WMGFXOXVVRzlwYm5TR0kyaDAKZEhBNkx5OTNkM2N1WVdobFkyRXVZMjR2WTNKc0wwRklRMEZUVFRJdVkzSnNNSUhTQmdnckJnRUZCUWNCQVFTQgp4VENCd2pDQml3WUlLd1lCQlFVSE1BS0dmMnhrWVhBNkx5OXNaR0Z3TG1Gb1pXTmhMbU51T2pNNE9TOURUajFCClNFTkJVMDB5TEVOT1BVRklRMEZUVFRJc0lFOVZQV05CUTJWeWRHbG1hV05oZEdWekxDQnZQV0ZvWTJFL1kwRkQKWlhKMGFXWnBZMkYwWlQ5aVlYTmxQMjlpYW1WamRFTnNZWE56UFdObGNuUnBabWxqWVhScGIyNUJkWFJvYjNKcApkSGt3TWdZSUt3WUJCUVVITUFLR0ptaDBkSEE2THk5M2QzY3VZV2hsWTJFdVkyNHZZMkZqWlhKMEwwRklRMEZUClRUSXVZMlZ5TUIwR0ExVWREZ1FXQkJSZy9HWndBOUtCcjBPTDBhckgxNVZYNWFzR2REQU1CZ2dxZ1J6UFZRR0QKZFFVQUEwY0FNRVFDSUh6Q2dQTlhhWkhSS1pOVWpBWXpGVGUvdUU2U1RJK002Nm80NU1TTXY1aUVBaUFRdG5kYwpHT203N3JDQVFmOFVwVDZnSnpPczdycDFCMXBWc3Z3RE00dFZ0UT09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K";
    private static String uuid = "c228c40837404a4aa8e4e1fb2c0c374b";
    
    @Test
    public void ooo() throws IOException {
        Map<String, String> map = new HashMap();
        Map<String, String> map2 = new HashMap();
        map.put("signData", signData);
        map.put("strCert", strCert);
        map.put("strData", uuid);
        map2.put("data", JSONObject.fromObject(map).toString());
        String postdata = JSONObject.fromObject(map2).toString();
        HttpPost httpPost = new HttpPost("http://60.167.89.95:8881/svsapi/api/verifyAuthP1");
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        StringEntity se = new StringEntity(postdata, "utf-8");
        se.setContentType("text/json");
        se.setContentEncoding(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
        httpPost.setEntity(se);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse =  httpClient.execute(httpPost);;
        String result = EntityUtils.toString(httpResponse.getEntity());
        System.out.println(result);
    }
}
