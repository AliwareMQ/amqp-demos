package com.mynotes.spring.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface AmqpGreetingChannel {

    String GREETING_IN = "greetingInChannel";
    String GREETING_OUT = "greetingOutChannel";

    @Output(AmqpGreetingChannel.GREETING_OUT)
    MessageChannel outGreeting();

    @Input(AmqpGreetingChannel.GREETING_IN)
    SubscribableChannel inGreeting();

}
