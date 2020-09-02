package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {

    @Bean
//    @LoadBalanced // 赋予restTemplate负载均衡能力，重写之后去掉这个注解
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
