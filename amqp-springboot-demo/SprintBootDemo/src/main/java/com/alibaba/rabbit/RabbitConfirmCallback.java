package com.alibaba.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author siyong.dxh@taobao.com
 * @Date 2019/4/23 8:22 PM
 */
public class RabbitConfirmCallback implements RabbitTemplate.ConfirmCallback {

    private static final Logger log= LoggerFactory.getLogger(RabbitConfirmCallback.class);

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("MessageConfirm correlationData: {}, ack: {}, cause: {}", correlationData, ack, cause);
    }
}
