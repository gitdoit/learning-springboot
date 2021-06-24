package org.seefly.mytomcat.model;

import lombok.Data;
import org.springframework.util.Assert;

/**
 * @author liujianxin
 * @date 2021/5/26 10:41
 */
@Data
public class CaAuthBody {
    private String signData;
    
    private String certdata;
    
    private String keySn;
    
    private String signType;
    
    public SOAServerAuthBody parse(String randomStr){
        Assert.notNull(signData,"签名数据不能为空!");
        Assert.notNull(certdata,"证书信息不能为空!");
        Assert.notNull(keySn,"UK信息不能为空!");
        Assert.notNull(randomStr,"服务器随机码不能为空!");
        SOAServerAuthBody body = new SOAServerAuthBody();
        SOAServerAuthBody.AuthData authData = new SOAServerAuthBody.AuthData();
        authData.setSignData(signData);
        authData.setStrCert(certdata);
        authData.setStrData(randomStr);
        body.setData(authData);
        return body;
    }
}
