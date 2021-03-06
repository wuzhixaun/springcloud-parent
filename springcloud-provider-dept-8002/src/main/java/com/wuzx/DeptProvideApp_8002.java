package com.wuzx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @program: springcloud-parent->DeptProvideApp
 * @description:Dept启动类
 * @author: wuzx
 * @create: 2019-08-14 15:57
 * @version: 1.0
 **/
@SpringBootApplication
@EnableEurekaClient //本服务启动后自动注册进Eureka服务中
@EnableDiscoveryClient //服务发现
public class DeptProvideApp_8002 {
    public static void main(String[] args) {
        SpringApplication.run(DeptProvideApp_8002.class,args);
    }
}
