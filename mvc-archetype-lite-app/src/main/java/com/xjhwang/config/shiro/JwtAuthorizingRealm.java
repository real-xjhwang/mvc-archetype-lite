package com.xjhwang.config.shiro;

import com.xjhwang.config.JwtProvider;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 携带Token时校验
 *
 * @author 黄雪杰 on 2024-07-11 15:55
 */
public class JwtAuthorizingRealm extends AuthorizingRealm {
    
    @Resource
    private JwtProvider jwtProvider;
    
    @Override
    public Class<?> getAuthenticationTokenClass() {
        
        return JwtAuthenticationToken.class;
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 暂时不处理授权
        return null;
    }
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取凭证
        String jwt = (String)token.getCredentials();
        // 解析凭证
        Claims claims;
        try {
            claims = jwtProvider.decode(jwt);
        } catch (Exception e) {
            throw new AuthenticationException("无效令牌", e);
        }
        // 签发人不匹配，无效token
        if (!jwtProvider.getIssuer().equals(claims.getIssuer())) {
            throw new AuthenticationException("无效令牌");
        }
        // token过期
        if (claims.getExpiration() != null && claims.getExpiration().before(new Date())) {
            throw new AuthenticationException("无效令牌");
        }
        return new SimpleAuthenticationInfo(claims.getSubject(), token.getCredentials(), getName());
    }
}
