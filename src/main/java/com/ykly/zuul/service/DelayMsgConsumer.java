package com.ykly.zuul.service;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.ykly.zuul.config.mq.AmqpConfig;
import com.ykly.zuul.entity.MQOrderMsg;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@RabbitListener(queues = AmqpConfig.DELAY_HANDLER_QUEUE)
public class DelayMsgConsumer {
    
    @Autowired
    private AmqpTemplate amqpTemplate;

    @RabbitHandler
    public void process(String hello, Channel channel, Message message) throws IOException {
        MQOrderMsg msg = JSON.parseObject(hello, MQOrderMsg.class);
        System.out.println("HelloReceiver收到  : " + msg +"收到时间"+new Date());
        try {
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            System.out.println("receiver success");
        } catch (Exception e) {
            e.printStackTrace();
            //丢弃这条消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            System.out.println("receiver fail");
        }

    }
}
