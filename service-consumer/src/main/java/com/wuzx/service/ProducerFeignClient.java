package com.wuzx.service;

import com.wuzx.service.fallback.ProducerFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @FeignClient表明当前类是一个Feign客户端,value指定客户端的服务名称
 */
@FeignClient(value = "service-producer", fallback = ProducerFeignClientFallback.class, path = "/resume")
public interface ProducerFeignClient {


    @GetMapping("/openstate/{userId}")
    public Integer findDefaultResumeState(@PathVariable("userId") Long userId);

}
