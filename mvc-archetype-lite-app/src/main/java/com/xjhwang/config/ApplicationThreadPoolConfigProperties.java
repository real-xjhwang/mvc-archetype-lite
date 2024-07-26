package com.xjhwang.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 应用内线程池配置属性
 *
 * @author 黄雪杰 on 2024-06-11 17:08
 */
@Data
@ConfigurationProperties(prefix = "application.thread.pool", ignoreInvalidFields = true)
public class ApplicationThreadPoolConfigProperties {
    
    private final String defaultRejectedExecutionHandlerClass = "java.util.concurrent.ThreadPoolExecutor$CallerRunsPolicy";
    
    /**
     * 核心线程数：默认为20
     */
    private Integer corePoolSize = 20;
    
    /**
     * 最大线程数：默认为50
     */
    private Integer maximumPoolSize = 50;
    
    /**
     * 非核心线程最大空闲时间
     */
    private Long keepAliveTime = 10L;
    
    /**
     * 最大等待任务数量
     */
    private Integer blockingQueueCapacity = 5000;
    
    /**
     * 饱和策略
     */
    private String rejectedExecutionHandler = defaultRejectedExecutionHandlerClass;
}
