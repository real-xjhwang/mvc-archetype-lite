package com.xjhwang.config;

import com.xjhwang.common.util.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 应用内线程池配置
 *
 * @author 黄雪杰 on 2024-06-11 17:08
 */
@Slf4j
@EnableAsync
@Configuration
@EnableConfigurationProperties(ApplicationThreadPoolConfigProperties.class)
public class ApplicationThreadPoolConfig {
    
    private static final String APPLICATION_THREAD_POOL_EXECUTOR_BEAN_NAME = "threadPoolExecutor";
    
    @Bean(name = APPLICATION_THREAD_POOL_EXECUTOR_BEAN_NAME)
    @ConditionalOnMissingBean(ThreadPoolExecutor.class)
    public ThreadPoolExecutor threadPoolExecutor(ApplicationThreadPoolConfigProperties properties) {
        // 处理饱和策略
        RejectedExecutionHandler rejectedExecutionHandler;
        try {
            rejectedExecutionHandler = ReflectUtils.newInstance(properties.getRejectedExecutionHandler());
            log.info("线程池拒绝策略实例化成功：{}", properties.getRejectedExecutionHandler());
        } catch (Exception e) {
            log.error("线程池拒绝策略实例化异常，使用默认拒绝策略：{}", properties.getDefaultRejectedExecutionHandlerClass(), e);
            rejectedExecutionHandler = ReflectUtils.newInstance(properties.getDefaultRejectedExecutionHandlerClass());
        }
        
        return new ThreadPoolExecutor(properties.getCorePoolSize(),
            properties.getMaximumPoolSize(),
            properties.getKeepAliveTime(),
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(properties.getBlockingQueueCapacity()),
            rejectedExecutionHandler);
    }
}
