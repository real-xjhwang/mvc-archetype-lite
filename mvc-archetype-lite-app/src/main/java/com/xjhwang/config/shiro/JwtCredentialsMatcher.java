package com.xjhwang.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * @author xjhwang on 2024/7/15 22:20
 */
public class JwtCredentialsMatcher implements CredentialsMatcher {
    
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        
        String token = (String)authenticationToken.getCredentials();
        return token.equals(authenticationInfo.getCredentials());
    }
}
