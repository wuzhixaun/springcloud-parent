package com.wuzx.service;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;

@EnableBinding(Sink.class)
public class MessageConsumerServiceImpl {


    @StreamListener(Sink.INPUT)
    public void receiveMessage(Message<String> message) {
        System.out.println("========接受消息:" + message);
    }
}
