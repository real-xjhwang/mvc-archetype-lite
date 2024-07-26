package com.xjhwang.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis客户端配置参数
 *
 * @author 黄雪杰 on 2024-06-11 17:33
 */
@Data
@ConfigurationProperties(prefix = "application.redis", ignoreInvalidFields = true)
public class RedisClientConfigProperties {
    
    private String host;
    
    private int port;
    
    private String password;
    
    /**
     * 连接池大小，默认为64
     */
    private int connectionPoolSize = 64;
    
    /**
     * 最小空闲连接数，这些连接将会一直被保持在连接池中，默认为20
     */
    private int connectionMinimumIdleSize = 20;
    
    /**
     * 空闲连接超时关闭时间，超过最小空闲连接数且超过这个时长的空闲连接将被关闭，默认为10000ms
     */
    private int idleConnectionTimeout = 10000;
    
    /**
     * 建立连接等待时间，超过这个时长还没有建立连接将会抛出异常，默认为10000ms
     */
    private int connectionTimeout = 10000;
    
    /**
     * 建立连接失败后的重试次数，默认为3次
     */
    private int retryAttempts = 3;
    
    /**
     * 建立连接失败后重试之间的间隔时长，默认为1000ms
     */
    private int retryInterval = 1000;
    
    /**
     * 定期检查连接是否可用的时间间隔，默认为0ms，即不检查
     */
    private int pingConnectionInterval = 0;
    
    /**
     * 是否保持长连接，默认为true
     */
    private boolean keepAlive = true;
    
    /**
     * 使用的数据库，默认为0
     */
    private int database = 0;
}
