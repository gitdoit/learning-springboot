package org.seefly.springsecurity.service;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author liujianxin
 * @date 2018-12-12 15:14
 */
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
            details.setAuthorizedGrantTypes(Collections.singletonList("authorization_code"));
        }else if("client1".equals(clientId)){
            details = new BaseClientDetails();
            details.setClientId("client1");
            details.setClientSecret("123");
            details.setScope(Collections.singletonList("delete"));
            details.setAuthorizedGrantTypes(Collections.singletonList("client_credentials"));
        }
        return details;
    }
}
