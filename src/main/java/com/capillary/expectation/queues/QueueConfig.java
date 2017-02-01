/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 31-Jan-2017
 */
package com.capillary.expectation.queues;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Config of all the queues being used
 * @author aditya
 *
 */
@Component
public class QueueConfig {

    private static final Logger logger = LoggerFactory.getLogger(QueueConfig.class);

    final static String eventQueue = "all-events";

    @PostConstruct
    private void init() {
        logger.info("initialized queue config");
    }

    @Bean
    Queue queue() {
        return new Queue(eventQueue, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("all-events-exchange");
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(eventQueue);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(eventQueue);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(EventConsumer receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

}
