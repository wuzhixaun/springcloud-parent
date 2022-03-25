package com.wuzx.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface LogProcessor {

    String INPUT_LOG = "input_log";
    String OUTPUT_LOG = "output_log";

    @Input(INPUT_LOG)
    SubscribableChannel inputLog();

    @Output(OUTPUT_LOG)
    MessageChannel outputLog();
}
