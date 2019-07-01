package com.alibaba.rabbit;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class Sender {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send() {
		String exchange = "exchange-rabbit-springboot-advance5";
		String routingKey = "product";
		String message = LocalDateTime.now().toString() + "发送一条消息.";
		rabbitTemplate.convertAndSend(exchange, routingKey, message,
			new CorrelationData("unRouting-" + UUID.randomUUID().toString()));

	}
}