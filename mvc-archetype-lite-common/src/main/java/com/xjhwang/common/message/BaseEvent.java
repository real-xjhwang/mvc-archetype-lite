package com.xjhwang.common.message;

import com.xjhwang.common.util.IdUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 事件
 *
 * @author 黄雪杰 on 2024-06-11 16:42
 */
@Data
public abstract class BaseEvent<T> {
    
    /**
     * 构建事件消息（大多数情况下的默认实现，如果有不同的情况，可在子类中自主实现）
     *
     * @param data 业务内容
     * @return 事件消息
     */
    public EventMessage<T> buildEventMessage(T data) {
        
        return EventMessage.<T>builder()
            .id(IdUtils.getSnowflakeNextIdStr())
            .timestamp(new Date())
            .data(data)
            .build();
    }
    
    /**
     * 消息主题
     * <p/>
     * 1. 用于发送消息到MQ时使用的topic
     *
     * @return 消息主题
     */
    public abstract String topic();
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventMessage<T> {
        
        /**
         * 消息唯一标识
         */
        private String id;
        
        /**
         * 消息生成时间
         */
        private Date timestamp;
        
        /**
         * 消息业务内容
         */
        private T data;
    }
}
