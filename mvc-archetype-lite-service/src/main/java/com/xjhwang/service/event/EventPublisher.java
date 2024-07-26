package com.xjhwang.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * @author 黄雪杰 on 2024-07-26 10:53
 */
@Slf4j
@Component
public class EventPublisher implements ApplicationContextAware {
    
    private static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        
        EventPublisher.applicationContext = applicationContext;
    }
    
    public void publish(ApplicationEvent event) {
        
        applicationContext.publishEvent(event);
    }
}
