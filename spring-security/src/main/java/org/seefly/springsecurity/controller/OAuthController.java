package org.seefly.springsecurity.controller;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * http://projects.spring.io/spring-security-oauth/docs/oauth2.html
 * @author liujianxin
 */
@Controller
public class OAuthController {


    @GetMapping("/users/me")
    public @ResponseBody Principal me(Principal principal) {
        return principal;
    }

    @RequestMapping("/oauth/confirm_access")
    public String confirm(AuthorizationRequest authorizationRequest, HttpServletRequest request){
        System.out.println(authorizationRequest);
        return "/oauth/confirm";
    }
}
