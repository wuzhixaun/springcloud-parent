package com.wuzx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient // 开启Eureka client
//@EnableDiscoveryClient // 开启注册中心客户端 (通用性注解，比如注册到Eureka、Nacos)
public class ServiceProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceProducerApplication.class, args);
    }
}
