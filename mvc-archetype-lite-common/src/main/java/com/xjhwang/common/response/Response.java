package com.xjhwang.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 请求响应
 *
 * @author 黄雪杰 on 2024-06-11 16:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable {
    
    private static final long serialVersionUID = -3262606219438715733L;
    
    /**
     * 响应码
     */
    private String code;
    
    /**
     * 描述信息
     */
    private String info;
    
    /**
     * 业务数据
     */
    private T data;
}
