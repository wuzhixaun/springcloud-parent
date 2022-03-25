package com.wuzx.controller;


import com.wuzx.service.ProducerFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autofeign")
public class AutoFeignController {


    @Autowired
    private ProducerFeignClient producerFeignClient;

    @Value("${ribbon.ConnectTimeout}")
    String ConnectTimeout;

    @GetMapping("/checkState/{userId}")
    public Integer findResumeOpenState(@PathVariable Long userId) {
        System.out.println(ConnectTimeout);
        return producerFeignClient.findDefaultResumeState(userId);
    }
}
