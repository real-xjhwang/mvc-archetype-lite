package com.xjhwang.config.shiro;

import com.xjhwang.common.response.ResponseCode;
import com.xjhwang.common.util.IdUtils;
import com.xjhwang.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 黄雪杰 on 2024-07-11 18:12
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        
        if (!isLoginAttempt(request, response)) {
            return false;
        }
        try {
            executeLogin(request, response);
            return true;
        } catch (Exception e) {
            if (e instanceof ShiroException) {
                throw (ShiroException) e;
            }
            throw new ShiroException(e);
        }
    }
    
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        
        String token = getAuthzHeader(request);
        return StringUtils.isNotBlank(token);
    }
    
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        
        String token = getAuthzHeader(request);
        return new JwtAuthenticationToken(IdUtils.getSnowflakeNextIdStr(), token);
    }
    
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        
        request.setAttribute("exception", new AuthenticationException(ResponseCode.UNAUTHORIZED.getInfo()));
        request.getRequestDispatcher("/v1/error/unauthorized").forward(request, response);
        return false;
    }
    
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        
        request.setAttribute("exception", e);
        try {
            request.getRequestDispatcher("/v1/error/unauthorized").forward(request, response);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return false;
    }
    
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域发送一个option请求
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
