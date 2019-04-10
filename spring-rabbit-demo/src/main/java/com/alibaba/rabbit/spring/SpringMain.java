package com.alibaba.rabbit.spring;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringMain {

    public static void main(final String... args) throws Exception {

        AbstractApplicationContext ctx =
            new ClassPathXmlApplicationContext("spring-rabbitmq.xml");
        RabbitTemplate template = ctx.getBean(RabbitTemplate.class);
        template.convertAndSend("Hello, world!");
        int i=0;
        while(i<10000){
            Thread.sleep(1000);
            i++;
        }
        ctx.destroy();
    }
}
