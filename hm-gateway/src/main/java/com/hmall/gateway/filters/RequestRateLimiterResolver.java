package com.hmall.gateway.filters;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RequestRateLimiterResolver {
    @Bean
    @Primary
    KeyResolver apiKeyResolver(){
        // 根据url限流，即以每秒请求数按URL分组统计，超出限流的url请求返回429
        return exchange -> Mono.just(exchange.getRequest().getPath().toString());
    }

    @Bean
    KeyResolver ipKeyResolver(){
        // 根据ip限流，超出限流的url请求返回429
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

}
