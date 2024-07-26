package com.xjhwang.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Web配置属性
 *
 * @author 黄雪杰 on 2024-06-11 18:06
 */
@Data
@Component
@ConfigurationProperties(prefix = "application.web")
public class WebConfigProperties {
    
    /**
     * 跨域限制
     */
    private String crossOrigin;
}
