package com.hmall.seckill;

import com.hmall.api.config.DefaultFeignConfig;
import com.hmall.seckill.properties.RedisProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties(RedisProperties.class)
@EnableFeignClients(basePackages = "com.hmall.api", defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("com.hmall.seckill.mapper")
public class SeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }
}