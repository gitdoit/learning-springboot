package org.seefly.springsecurity.custom.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liujianxin
 * @date 2018-12-13 19:48
 */
@Component
public class LogoutSuccessHandle extends AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {
    private static final String BEARER_AUTHENTICATION = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "authorization";
    @Resource
    private TokenStore tokenStore;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)  {
        String token = request.getHeader(HEADER_AUTHORIZATION);
        if (token!=null&&token.startsWith(BEARER_AUTHENTICATION)){
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token.split(" ")[0]);
            if (oAuth2AccessToken!=null){
                tokenStore.removeAccessToken(oAuth2AccessToken);
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
