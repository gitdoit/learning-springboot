package org.seefly.springsecurity.custom.service;

import org.seefly.springsecurity.mapper.ClientDetailsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 *
 * clientId：客户端标识 ID
 * secret：客户端安全码
 * scope：客户端访问范围，默认为空则拥有全部范围
 * authorizedGrantTypes：客户端使用的授权类型，默认为空
 * authorities：客户端可使用的权限
 * @author liujianxin
 * @date 2018-12-12 15:14
 * @see <a href="https://stackoverflow.com/questions/32092749/spring-oauth2-scope-vs-authoritiesroles">scope vs authorities</a>
 * @see <a href="https://tools.ietf.org/html/rfc6749#section-3.3">RFC-6749</a>
 */
@Primary
@Service
public class MyClientDetailsServiceImpl implements ClientDetailsService {

    @Autowired
    private ClientDetailsMapper clientDetailsMapper;



    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        org.seefly.springsecurity.entity.ClientDetails byClientId = clientDetailsMapper.findByClientId(clientId);
        if(Objects.isNull(byClientId)){
            throw new NoSuchClientException("没有这个客户端！");
        }
        BaseClientDetails details = new BaseClientDetails();
        BeanUtils.copyProperties(byClientId,details);
        return details;
    }
}
