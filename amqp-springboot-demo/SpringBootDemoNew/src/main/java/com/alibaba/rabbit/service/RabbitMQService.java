package com.alibaba.rabbit.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void declare() {
        /*
         * 声明exchange，若不存在则创建，共包含四种类型的exchange：direct、fanout、topic、headers。
         * 构造函数 xxExchange() 参数：
         * name       交换机名称
         * durable    是否持久化，默认为true
         * autoDelete 是否自动删除，默认为false
         * arguments  额外参数，默认为null
         */
        rabbitAdmin.declareExchange(new DirectExchange("myDirectExchange", true, false, null));
        rabbitAdmin.declareExchange(new FanoutExchange("myFanoutExchange", true, false, null));
        rabbitAdmin.declareExchange(new TopicExchange("myTopicExchange", true, false, null));
        rabbitAdmin.declareExchange(new HeadersExchange("myHeadersExchange", true, false, null));

        /*
         * 声明queue，若不存在则创建。
         * 构造函数 Queue() 参数：
         * name       队列名称
         * durable    是否持久化，默认为true
         * exclusive  是否独占，默认为false
         * autoDelete 是否自动删除，默认为false
         */
        rabbitAdmin.declareQueue(new Queue("myQueue2", true, false, false));

        /*
         * 绑定操作
         * 构造函数 Binding() 参数：
         * destination 需要绑定的队列/交换机的名称
         * destinationType 绑定类型：Binding.DestinationType.QUEUE 队列；Binding.DestinationType.EXCHANGE 交换机
         * exchange 交换机名称
         * routingKey 路由key
         * arguments 额外参数
         */
        rabbitAdmin.declareBinding(new Binding("myQueue2",
            Binding.DestinationType.QUEUE, "myDirectExchange", "routingDirect", new HashMap<>()));
    }

    public void sendMessage(String exchange, String routingKey, String content) {
        // 设置MessageId
        String msgId = UUID.randomUUID().toString();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setMessageId(msgId);
        // 创建Message
        Message message = new Message(content.getBytes(StandardCharsets.UTF_8), messageProperties);
        /*
         * 发送消息 send() 参数：
         * exchange 交换机名称
         * routingKey 路由key
         * message 消息
         * correlationData 用于关联发布者确认
         */
        rabbitTemplate.send(exchange, routingKey, message, null);
    }
}

