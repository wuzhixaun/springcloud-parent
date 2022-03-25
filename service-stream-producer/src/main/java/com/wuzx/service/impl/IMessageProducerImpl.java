package com.wuzx.service.impl;

import com.wuzx.service.IMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;

@EnableBinding(Source.class)
public class IMessageProducerImpl implements IMessageProducer {

    // 将MessageChannel的封装对象Source注入到这里使用
    @Autowired
    private Source source;

    @Override
    public void sendMessage(String content) {
        /**
         * 向mq中发送消息(并不是直接操作mq，应该操作的是spring cloud stream)
         * 使用通道向外发出消息(指的是Source里面的output通道)
         */
        source.output().send(MessageBuilder.withPayload(content).build());
    }
}
