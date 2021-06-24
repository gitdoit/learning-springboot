package org.seefly.mytomcat.model;

import lombok.Data;

/**
 * @author liujianxin
 * @date 2021/5/26 10:42
 */
@Data
public class SOAServerAuthBody {
    private AuthData data;
    
    @Data
    public static class AuthData{
        private String signData;
        private String strCert;
        private String strData;
    }
    
}
