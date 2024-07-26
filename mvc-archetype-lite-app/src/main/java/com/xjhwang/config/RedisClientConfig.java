package com.xjhwang.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redis客户端配置
 *
 * @author 黄雪杰 on 2024-06-11 17:58
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RedisClientConfigProperties.class)
public class RedisClientConfig {
    
    private static final String REDISSON_CLIENT_BEAN_NAME = "redissonClient";
    
    @Bean(name = REDISSON_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(RedisClientConfigProperties properties) {
        
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);
        
        config.useSingleServer()
            .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
            .setPassword(properties.getPassword())
            .setConnectionPoolSize(properties.getConnectionPoolSize())
            .setConnectionMinimumIdleSize(properties.getConnectionMinimumIdleSize())
            .setIdleConnectionTimeout(properties.getIdleConnectionTimeout())
            .setConnectTimeout(properties.getConnectionTimeout())
            .setRetryAttempts(properties.getRetryAttempts())
            .setRetryInterval(properties.getRetryInterval())
            .setKeepAlive(properties.isKeepAlive())
            .setDatabase(properties.getDatabase());
        return Redisson.create(config);
    }
}
