package com.alibaba.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    @RabbitListener(queues = "queue-rabbit-springboot-advance5")
    public void process(String hello) {
        System.out.println("Receiver : " + hello);
    }
}
