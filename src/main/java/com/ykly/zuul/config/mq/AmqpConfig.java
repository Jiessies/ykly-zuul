package com.ykly.zuul.config.mq;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AmqpConfig {

    public static final String DELAY_EXCHANGE = "delay-exchange";

    public static final String DELAY_ROUTING_KEY = "delay-routing-key";

    public static final String DELAY_QUEUE = "delay-queue";

    public static final String DELAY_HANDLER_EXCHANGE = "delay-handler-exchange";

    public static final String DELAY_HANDLER_ROUTING_KEY = "delay-handler-routing-key";

    public static final String DELAY_HANDLER_QUEUE = "delay-handler-queue";

    @Bean
    TopicExchange delayExchange() {
        return new TopicExchange(DELAY_EXCHANGE, true, false);
    }

    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DELAY_HANDLER_EXCHANGE);
        args.put("x-dead-letter-routing-key", DELAY_HANDLER_ROUTING_KEY);
        return new Queue(DELAY_QUEUE, true, false, false, args);
    }

    @Bean
    public Binding delayBinding(Queue delayQueue, TopicExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY_ROUTING_KEY);
    }

    @Bean
    public TopicExchange delayHandlerExchange() {
        return new TopicExchange(DELAY_HANDLER_EXCHANGE, true, false);
    }

    @Bean
    public Queue delayHandlerQueue() {
        return new Queue(DELAY_HANDLER_QUEUE, true, false, false);
    }

    @Bean
    public Binding delayHandlerBinding(Queue delayHandlerQueue, TopicExchange delayHandlerExchange) {
        return BindingBuilder.bind(delayHandlerQueue).to(delayHandlerExchange).with(DELAY_HANDLER_ROUTING_KEY);
    }

}