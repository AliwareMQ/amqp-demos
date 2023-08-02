package com.alibaba.rabbit.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Bean
    public ConnectionFactory connectionFactory() {
        // 初始化RabbitMQ连接配置 connectionFactory
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        // 开启Connection自动恢复功能
        connectionFactory.getRabbitConnectionFactory().setAutomaticRecoveryEnabled(true);
        /*
         * 设置缓存模式，其中：
         * CacheMode.CHANNEL，程序运行期间ConnectionFactory只维护着一个connection，并缓存多个channel;
         * CacheMode.CONNECTION，在CONNECTION模式下，可以创建多个connection，程序会缓存一定数量的connection，每个connection中缓存一定数量的channel。
         */
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
        // CONNECTION模式下，缓存的connection数量
        connectionFactory.setConnectionCacheSize(10);
        // 缓存中保持的channel数量
        connectionFactory.setChannelCacheSize(64);
        return connectionFactory;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        // RabbitMQ 消息模板，该模板封装了多种常用的消息操作
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        // RabbitAdmin封装了对 RabbitMQ 管理端的操作
        return new RabbitAdmin(connectionFactory);
    }
}
