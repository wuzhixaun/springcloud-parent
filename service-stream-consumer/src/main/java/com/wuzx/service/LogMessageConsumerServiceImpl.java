package com.wuzx.service;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

@EnableBinding(LogProcessor.class)
public class LogMessageConsumerServiceImpl {


    @StreamListener(LogProcessor.INPUT_LOG)
    public void receiveMessage(Message<String> message) {
        System.out.println("========LOG接受消息:" + message);
    }
}
