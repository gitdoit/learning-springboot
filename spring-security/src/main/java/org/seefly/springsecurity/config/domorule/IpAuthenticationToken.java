package org.seefly.springsecurity.config.domorule;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 使用IP地址认证方式的认证信息封装
 * demo来自
 * @see <a href="https://mp.weixin.qq.com/s?__biz=MzAxODcyNjEzNQ==&mid=2247484370&idx=1&sn=7a326c0c95792a0863e0f1e86af34b78&chksm=9bd0ae4aaca7275c1ccc1889ae29a104e03b7a5175a28534a2428a12e5729583a026aa544f81&scene=21#wechat_redirect"/>原文链接</a>
 * @author liujianxin
 * @date 2018-12-17 16:20
 */
public class IpAuthenticationToken extends AbstractAuthenticationToken {
    private String ip;

    /**
     * 认证之前的构造器
     */
    public IpAuthenticationToken(String ip) {
        super(null);
        this.ip = ip;
        // 状态为未认证
        super.setAuthenticated(false);
    }

    /**
     * 认证之后的构造器
     *
     * @param authorities 权限
     * @param ip ip地址
     */
    public IpAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String ip) {
        super(authorities);
        // 状态为已认证
        super.setAuthenticated(true);
        this.ip = ip;
    }

    public String getIp(){
        return this.ip;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.ip;
    }
}
