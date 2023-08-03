package com.alibaba.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;


@Component
public class SenderWithCallback {
    private static final Logger log = LoggerFactory.getLogger(SenderWithCallback.class);
    @Autowired
    private  RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initRabbitTemplate() {
        // 设置生产者消息确认
        rabbitTemplate.setConfirmCallback(new RabbitConfirmCallback());
        rabbitTemplate.setReturnCallback(new RabbitReturnCallback());
    }

    public void send() {
        String exchange = "exchange-rabbit-springboot-advance";
        String unRoutingKey = "unProduct";

        //1.发送一条消息
		MessageProperties messageProperties = new MessageProperties();
		String msgId = "unRouting-" + UUID.randomUUID().toString();
        //此处设置的msgId才能被会转成rabbitmq client的messageId，发送给broker
		messageProperties.setMessageId(msgId);
        Message message = new Message("发送一条消息".getBytes(), messageProperties);
		
		rabbitTemplate.convertAndSend(exchange, unRoutingKey, message, new CorrelationData(msgId));
        
        log.info("发送一条消息,exchange:[{}],routingKey:[{}],message:[{}]", exchange, unRoutingKey, message);
    }

}
