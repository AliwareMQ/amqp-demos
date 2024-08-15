package com.mynotes.spring.consumer;

import com.mynotes.spring.channel.AmqpGreetingChannel;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class AckModelChecker {
    @Autowired
    private Environment env;
    String ACK_MODE_PROPERTY = "spring.cloud.stream.rabbit.bindings.greetingInChannel.consumer.acknowledge-mode";

    public boolean isManualAckModel() {
        String ackMode = env.getProperty(ACK_MODE_PROPERTY);
        return "MANUAL".equalsIgnoreCase(ackMode);
    }
}
