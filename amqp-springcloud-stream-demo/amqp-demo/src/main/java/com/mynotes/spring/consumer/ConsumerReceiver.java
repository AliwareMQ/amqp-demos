package com.mynotes.spring.consumer;

import com.mynotes.spring.channel.AmqpGreetingChannel;
import com.mynotes.spring.service.AmqpConsumeService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ConsumerReceiver {

    @Resource
    private AckModelChecker ackModelChecker;

    @Resource
    private AmqpConsumeService amqpConsumeService;

    @StreamListener(target = AmqpGreetingChannel.GREETING_IN)
    public void consume(Message<String> msg, @Header(AmqpHeaders.CHANNEL) Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        String msgBody = msg.getPayload();
        System.out.println("[x]Received message from greeting channel:" + msg);
        boolean consumeSuccess = amqpConsumeService.processMessage(msgBody);
        if (ackModelChecker.isManualAckModel()) {
            try {
                if (consumeSuccess) {
                    channel.basicAck(deliveryTag, false);
                    System.out.println("[   x]Success consuming message from greeting channel:" + msg);
                } else {
                    channel.basicNack(deliveryTag, false, true);
                    System.out.println("[   x]Failed consuming message from greeting channel:" + msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("[   x]Auto-Ack message to greeting channel:" + msg);
        }
    }
}
