package com.alibaba.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.UUID;


@Component
public class SenderWithCallback {
    Logger log= LoggerFactory.getLogger(SenderWithCallback.class);
    @Autowired
    private  RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initRabbitTemplate() {
        // 设置生产者消息确认
        rabbitTemplate.setConfirmCallback(new RabbitConfirmCallback());
        rabbitTemplate.setReturnCallback(new RabbitReturnCallback());
    }


    public void send() {
        String exchange = "exchange-rabbit-springboot-advance5";
        String unRoutingKey = "norProduct";

        //1.发送一条未被路由的消息
		MessageProperties messageProperties = new MessageProperties();
		String msgId = "unRouting-" + UUID.randomUUID().toString();
        //此处设置的msgId才能被会转成rabbitmq client的messageId，发送给broker
		messageProperties.setMessageId(msgId);
        Message message = new Message("发送一条消息".getBytes(), messageProperties);
		
		rabbitTemplate.convertAndSend(exchange, routingKey, message, new CorrelationData(msgId));
        
        log.info("发送一条消息,exchange:[{}],routingKey:[{}],message:[{}]", exchange, unRoutingKey, message);



    }

}
