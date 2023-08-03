package com.alibaba.rabbit;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Autowired
    private RabbitProperties rabbitProperties;

    @Bean
    public ConnectionFactory getConnectionFactory() throws NoSuchAlgorithmException, KeyManagementException {
        // 初始化RabbitMQ连接配置 connectionFactory
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
        // 开启Connection自动重连功能
        connectionFactory.getRabbitConnectionFactory().setAutomaticRecoveryEnabled(true);
        /*
         * 使用云消息队列 RabbitMQ，推荐使用CacheMode.CONNECTION 模式（建多个connection，程序会缓存一定数量的connection，每个connection中缓存一定数量的channel）。
         * 云消息队列 RabbitMQ 是集群分布式架构，在CONNECTION模式下，创建多个connection 可以更好地和集群的多个MQ服务节点连接，更高效的发送和消费消息。。
         */
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
        // CONNECTION模式下，缓存的connection数量
        connectionFactory.setConnectionCacheSize(10);
        // 缓存中保持的channel数量
        connectionFactory.setChannelCacheSize(64);

        connectionFactory.setPublisherConfirms(rabbitProperties.isPublisherConfirms());
        connectionFactory.setPublisherReturns(rabbitProperties.isPublisherReturns());

        // 开启数据传输加密
//        connectionFactory.getRabbitConnectionFactory().useSslProtocol();
//        connectionFactory.setPort(5671);

        return connectionFactory;
    }

    /**
     * 申明队列
     *
     * @return
     */
    @Bean
    public Queue queue() {
        Map<String, Object> arguments = new HashMap<>(4);

        return new Queue("queue-rabbit-springboot-advance", true, false, false, arguments);
    }

    @Bean
    public Exchange exchange() {
        Map<String, Object> arguments = new HashMap<>(4);

        return new DirectExchange("exchange-rabbit-springboot-advance", true, false, arguments);
    }

    /**
     * 申明绑定
     *
     * @return
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("product").noargs();
    }
}
