package com.xjhwang.config;

import com.xjhwang.common.exception.ApplicationException;
import com.xjhwang.common.response.Response;
import com.xjhwang.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author xjhwang on 2024/7/15 22:03
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ShiroException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Response<String> handleShiroException(ShiroException e) {
        
        log.error(e.getMessage(), e);
        
        return Response.<String>builder()
            .code(ResponseCode.UNAUTHORIZED.getCode())
            .info(ResponseCode.UNAUTHORIZED.getInfo())
            .build();
    }
    
    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<String> handleApplicationException(ApplicationException e) {
        
        log.error(e.getMessage(), e);
        
        return Response.<String>builder()
            .code(e.getCode())
            .info(e.getInfo())
            .build();
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response<String> handleException(Exception e) {
        
        log.error(e.getMessage(), e);
        
        return Response.<String>builder()
            .code(ResponseCode.UNKNOWN_ERROR.getCode())
            .info(ResponseCode.UNKNOWN_ERROR.getInfo())
            .build();
    }
}
