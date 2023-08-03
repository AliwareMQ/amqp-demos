package com.alibaba.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Sender {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send() {
		String exchange = "exchange-rabbit-springboot-advance";
		String routingKey = "product";
		
		MessageProperties messageProperties = new MessageProperties();
		String msgId = "routing-" + UUID.randomUUID().toString();
                //此处设置的msgId才能被会转成rabbitmq client的messageId，发送给broker
		messageProperties.setMessageId(msgId);
                Message message = new Message("发送一条消息".getBytes(), messageProperties);
		
		rabbitTemplate.convertAndSend(exchange, routingKey, message,
			new CorrelationData(msgId));

	}
}
