package com.alibaba.rabbit.spring;

import org.springframework.stereotype.Component;


@Component
public class MessageReceiver {
    public void listen(String message) {
        System.out.println(" [x] Received '" + message + "'");
    }
}
