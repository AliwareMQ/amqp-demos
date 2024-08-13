package com.alibaba.rabbit.spring;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringMain {

    public static void main(final String... args) throws Exception {

        AbstractApplicationContext ctx =
                new ClassPathXmlApplicationContext("spring-rabbitmq.xml");
        RabbitTemplate template = ctx.getBean(RabbitTemplate.class);
        int i = 0;
        while (i < 10) {
            String message = "Hello World, I am AMQP [" + i + "]!";
            template.convertAndSend(message);
            System.out.println(" [-] Send '" + message + "'");
            Thread.sleep(2000);
            i++;
        }
        ctx.destroy();
    }
}
