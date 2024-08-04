package com.hmall.seckill.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

//@Configuration
//@RequiredArgsConstructor
//public class RabbitConfig {
//
//
//    private final RabbitTemplate rabbitTemplate;
//
//    @PostConstruct
//    public void init() {
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//    }
//}
