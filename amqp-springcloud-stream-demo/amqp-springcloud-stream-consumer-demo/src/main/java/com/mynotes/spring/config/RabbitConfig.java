package com.mynotes.spring.config;


import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
    //资源owner账户 ID 信息
    private static final String INSTANCE_ID ="XXX";
    @Autowired
    private RabbitProperties rabbitProperties;


    @Bean
    public ConnectionFactory getConnectionFactory() {
        com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory =
                new com.rabbitmq.client.ConnectionFactory();
        rabbitConnectionFactory.setHost(rabbitProperties.getHost());
        rabbitConnectionFactory.setPort(rabbitProperties.getPort());
        rabbitConnectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());

        AliyunCredentialsProvider credentialsProvider = new AliyunCredentialsProvider(
                rabbitProperties.getUsername(), rabbitProperties.getPassword(), INSTANCE_ID);
        rabbitConnectionFactory.setCredentialsProvider(credentialsProvider);
        //rabbitConnectionFactory.setAutomaticRecoveryEnabled(true);
        rabbitConnectionFactory.setNetworkRecoveryInterval(5000);
        ConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitConnectionFactory);
        ((CachingConnectionFactory)connectionFactory).setPublisherConfirms(rabbitProperties.isPublisherConfirms());
        ((CachingConnectionFactory)connectionFactory).setPublisherReturns(rabbitProperties.isPublisherReturns());
        return connectionFactory;
    }

    ///**
    // * 申明队列
    // *
    // * @return
    // */
    //@Bean
    //public Queue queue() {
    //    Map<String, Object> arguments = new HashMap<>(4);
    //
    //    return new Queue("queue-rabbit-springboot-advance5", true, false, false, arguments);
    //}
    //
    //@Bean
    //public Exchange exchange() {
    //    Map<String, Object> arguments = new HashMap<>(4);
    //
    //    return new DirectExchange("exchange-rabbit-springboot-advance5", true, false, arguments);
    //}
    //
    ///**
    // * 申明绑定
    // *
    // * @return
    // */
    //@Bean
    //public Binding binding() {
    //    return BindingBuilder.bind(queue()).to(exchange()).with("product").noargs();
    //}


}