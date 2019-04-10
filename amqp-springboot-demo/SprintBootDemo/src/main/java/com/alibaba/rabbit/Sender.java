package com.alibaba.rabbit;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.UUID;

@Component
public class Sender {
	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send() {
		String content = "ts= " + new Date() + ", content= " + UUID.randomUUID().toString();
		//System.out.println("Sender : " + content);
		this.rabbitTemplate.convertAndSend("queue", content);
	}
}