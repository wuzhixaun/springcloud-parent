package com.wuzx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/autodeliver")
public class AutodeliverController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/checkState/{userId}")
    public Integer findResumeOpenState(@PathVariable Long userId) {


        // 1.从Eureka Server获取resumer 服务实例信息
        final List<ServiceInstance> instances = discoveryClient.getInstances("service-resume");

        // 2.如果有多个实例，选择一个使用(负载均衡的过程)
        final ServiceInstance serviceInstance = instances.get(0);

        // 3. 元数据信息
        final String host = serviceInstance.getHost();
        final int port = serviceInstance.getPort();

        String url = String.format("http://%s:%s/resume/openstate/%s", host, port, userId);

        return restTemplate.getForObject(url, Integer.class);
    }
}
