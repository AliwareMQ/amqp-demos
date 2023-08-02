package com.alibaba.rabbit.listener;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    /**
     * 消息接收 手动确认ack
     * @param message     消息
     * @param deliveryTag messageProperties中的DELIVERY_TAG属性
     * @param channel     接收消息的channel
     * 注：队列myQueue需提前创建
     */
    @RabbitListener(queues = "myQueue")
    public void receiveFromMyQueue(
        Message message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,  Channel channel) throws IOException {
        System.out.println(new String(message.getBody()));
        // 进入消息消费业务逻辑

        // 手动确认ack
        channel.basicAck(deliveryTag, false);
    }

    /**
     * 消息接收 自动确认ack
     * @param message 消息
     * 注：应用程序启动时会声明newQueue和newExchange
     */
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "newQueue", durable = "true"),
        exchange = @Exchange(value = "newExchange"),
        key = "routingKey"
    ))
    public void receiveFromNewQueue(Message message) {
        System.out.println(new String(message.getBody()));
        // 进入消息消费业务逻辑
    }
}
