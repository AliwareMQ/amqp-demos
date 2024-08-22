package com.mynotes.spring.service;

import com.mynotes.spring.channel.AmqpGreetingChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AmqpGreetingServiceImpl implements AmqpGreetingService {

    @Resource
    private AmqpGreetingChannel channel;

    @Override
    public String greetAmqp(String body) {
        String greeting = "Hello, Spring Could Stream AMQP[" + body + "]!";
        System.out.println("[x]Sending greeting: " + greeting);
        Message<String> msg = MessageBuilder.withPayload(greeting)
                .setHeader("routingKey", "yunQi-routing-key")
                .build();
        this.channel.outGreeting().send(msg);
        return "Message Sent: " + greeting;
    }
}
