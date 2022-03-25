package com.wuzx.service.fallback;

import com.wuzx.service.ProducerFeignClient;
import org.springframework.stereotype.Component;

/**
 * 降级回退逻辑
 */
@Component
public class ProducerFeignClientFallback implements ProducerFeignClient {

    @Override
    public Integer findDefaultResumeState(Long userId) {
        return -10100;
    }
}
