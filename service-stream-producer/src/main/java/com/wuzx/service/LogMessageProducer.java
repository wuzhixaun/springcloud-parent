package com.wuzx.service;

public interface LogMessageProducer {
    /**
     * 发送文本消息
     *
     * @param content
     */
    public void sendMessage(String content);
}
