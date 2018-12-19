package org.seefly.springsecurity.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liujianxin
 * @date 2018-12-12 15:14
 */
@Primary
@Service
public class MyClientDetailsServiceImpl implements ClientDetailsService {


    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails details = null;
        if ("client".equals(clientId)) {
            details = new BaseClientDetails();
            details.setClientId("client");
            details.setClientSecret("123");
            details.setScope(Arrays.asList("read","delete"));
            details.setAuthorizedGrantTypes(Arrays.asList("authorization_code","refresh_token"));
        }else if("client1".equals(clientId)){
            details = new BaseClientDetails();
            details.setClientId("client1");
            details.setClientSecret("123");
            Set<String> s = new HashSet<>();
            s.add("http://baidu.com");
            details.setRegisteredRedirectUri(s);
            details.setScope(Collections.singletonList("delete"));
            details.setAuthorizedGrantTypes(Arrays.asList("authorization_code","refresh_token"));
        }else {
            throw new NoSuchClientException("没有这个客户端！");
        }

        return details;
    }
}
