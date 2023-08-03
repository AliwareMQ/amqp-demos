package com.alibaba.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author siyong.dxh@taobao.com
 * @Date 2019/4/23 8:22 PM
 */
public class RabbitReturnCallback implements RabbitTemplate.ReturnCallback {

    private static final Logger log= LoggerFactory.getLogger(RabbitReturnCallback.class);

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("message return message: {}, replyCode: {}, replyText: {}, exchange: {}, routingKey: {}",
            new String(message.getBody()), replyCode, replyText, exchange, routingKey);
    }
}
