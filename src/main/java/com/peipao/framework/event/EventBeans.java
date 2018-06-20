package com.peipao.framework.event;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.Environment;
import reactor.bus.EventBus;

/**
 * 方法名称：EventBeans
 * 功能描述：EventBus Bean
 * 作者：Liu Fan
 * 版本：1.0
 * 创建日期：2017/11/22 13:34
 * 修订记录：
 */

@Component
public class EventBeans {

    @Bean
    Environment env() {
        return Environment.initializeIfEmpty().assignErrorJournal();
    }

    @Bean
    EventBus createEventBus(Environment env) {
        return EventBus.create(env, Environment.THREAD_POOL);
    }

}
