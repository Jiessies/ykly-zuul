package com.ykly.zuul.config.mq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;


public class DelayPostProcessor implements MessagePostProcessor {

    private String delay_time = "1000";

    public DelayPostProcessor(String ttlTime) {
        this.delay_time = ttlTime;
    }

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        MessageProperties messageProperties = message.getMessageProperties();
        messageProperties.setExpiration(delay_time);
        messageProperties.setContentEncoding("UTF-8");
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        return message;
    }
}
