package com.alibaba.rabbit;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    private static final Logger log = LoggerFactory.getLogger(Receiver.class);

    /**
     * 消息接收 自动确认ack
     * @param message 消息
     */
//    @RabbitListener(queues = "queue-rabbit-springboot-advance")
//    public void process(Message message) {
//        log.info("receive message: {}", new String(message.getBody()));
//        // 进入业务消费逻辑
//    }

    /**
     * 消息接收 手动确认ack
     * @param message     消息
     * @param deliveryTag messageProperties中的DELIVERY_TAG属性
     * @param channel     接收消息的channel
     */
    @RabbitListener(queues = "queue-rabbit-springboot-advance")
    public void processMsgWithRequiredManualAck(Message message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
        Channel channel) throws IOException {
        log.info("receive message: {}", new String(message.getBody()));
        // 进入业务消费逻辑

        channel.basicAck(deliveryTag, false);
    }
}
