package org.seefly.springsecurity.custom;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * 令牌内容自定义,加一个组织信息
 * @author liujianxin
 * @date 2018-12-21 16:52
 */
public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>(1);
        additionalInfo.put("organization", authentication.getName() + RandomStringUtils.random(4,true,false));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
