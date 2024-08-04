package com.hmall.seckill.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisProperties {

    private String host;
    private int port;
    private String password;
    private long timeout;
    private int database;

}
