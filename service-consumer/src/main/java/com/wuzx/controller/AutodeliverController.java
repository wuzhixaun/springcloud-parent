package com.wuzx.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

//@RestController
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


    /**
     * 通过ribbon调用
     *
     * @param userId
     * @return
     */
    @GetMapping("/checkStateByRibbon/{userId}")
    public Integer findResumeOpenStateRibbon(@PathVariable Long userId) {
        String url = String.format("http://%s/resume/openstate/%s", "service-resume", userId);
        return restTemplate.getForObject(url, Integer.class);
    }

    /**
     * 通过ribbon调用
     *
     * @param userId
     * @return
     */
    // 使用HystrixCommand注解进行熔断控制
    @HystrixCommand(
            // commandProperties进行一些熔断细节属性配置
            commandProperties = {
                    @HystrixProperty(name =
                            "metrics.rollingStats.timeInMilliseconds",value = "8000"),
                    @HystrixProperty(name =
                            "circuitBreaker.requestVolumeThreshold",value = "2"),
                    @HystrixProperty(name =
                            "circuitBreaker.errorThresholdPercentage",value = "50"),
                    @HystrixProperty(name =
                            "circuitBreaker.sleepWindowInMilliseconds",value = "3000")
            }
    )
    @GetMapping("/checkStateTimeout/{userId}")
    public Integer findResumeOpenStateTimeout(@PathVariable Long userId) {

        String url = String.format("http://%s/resume/openstate/%s", "service-resume", userId);
        return restTemplate.getForObject(url, Integer.class);
    }


    @HystrixCommand(
            // commandProperties进行一些熔断细节属性配置
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
            },fallbackMethod = "myFallBack"
    )
    @GetMapping("/checkStateTimeoutFallBack/{userId}")
    public Integer checkStateTimeoutFallBack(@PathVariable Long userId) {

        String url = String.format("http://%s/resume/openstate/%s", "service-resume", userId);
        return restTemplate.getForObject(url, Integer.class);
    }


    /**
     * 定义回退方法，方法形参和返回值与原始方法一样
     */

    public Integer myFallBack(Long id) {
        return -1;
    }

}
