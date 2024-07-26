package com.xjhwang.config;

import com.xjhwang.common.util.MapUtils;
import com.xjhwang.config.shiro.JwtAuthorizingRealm;
import com.xjhwang.config.shiro.JwtCredentialsMatcher;
import com.xjhwang.config.shiro.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 黄雪杰 on 2024-07-11 10:55
 */
@Slf4j
@Configuration
public class GatewayConfig {
    
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") SecurityManager securityManager,
        @Qualifier("defaultShiroFilterChainDefinition") DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition) {
        
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/v1/sign-in");
        shiroFilterFactoryBean.setUnauthorizedUrl("/v1/error/unauthorized");
        
        Map<String, Filter> filters = MapUtils.builder(new HashMap<String, Filter>())
            .put("anon", new AnonymousFilter())
            .put("jwt", new JwtFilter())
            .build();
        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(defaultShiroFilterChainDefinition.getFilterChainMap());
        return shiroFilterFactoryBean;
    }
    
    @Bean("defaultWebSecurityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("gatewayAuthorizingRealm") JwtAuthorizingRealm jwtAuthorizingRealm) {
        
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(jwtAuthorizingRealm);
        // 关闭ShiroDao
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        // 不需要将Shiro Session中的东西存到任何地方
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        return defaultWebSecurityManager;
    }
    
    @Bean("defaultShiroFilterChainDefinition")
    public DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition() {
        
        DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();
        // 登录接口不需要认证
        defaultShiroFilterChainDefinition.addPathDefinition("/v1/auth/sign-in", "anon");
        // 注册接口不需要认证
        defaultShiroFilterChainDefinition.addPathDefinition("/v1/auth/sign-up", "anon");
        // 异常分发接口不需要认证
        defaultShiroFilterChainDefinition.addPathDefinition("/v1/error/**", "anon");
        // 其余接口都需要认证
        defaultShiroFilterChainDefinition.addPathDefinition("/**", "jwt");
        return defaultShiroFilterChainDefinition;
    }
    
    @Bean("gatewayAuthorizingRealm")
    public JwtAuthorizingRealm gatewayAuthorizingRealm(@Qualifier("jwtCredentialsMatcher") JwtCredentialsMatcher jwtCredentialsMatcher) {
        
        JwtAuthorizingRealm jwtAuthorizingRealm = new JwtAuthorizingRealm();
        jwtAuthorizingRealm.setCredentialsMatcher(jwtCredentialsMatcher);
        return jwtAuthorizingRealm;
    }
    
    @Bean("jwtCredentialsMatcher")
    public JwtCredentialsMatcher jwtCredentialsMatcher() {
        
        return new JwtCredentialsMatcher();
    }
    
    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        
        return new LifecycleBeanPostProcessor();
    }
    
    @Bean("defaultPasswordService")
    public DefaultPasswordService defaultPasswordService() {
        
        return new DefaultPasswordService();
    }
}
