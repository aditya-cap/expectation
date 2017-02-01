/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 31-Jan-2017
 */
package com.capillary.expectation.queues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author aditya
 *
 */
@Component
public class EventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Object message) throws Exception {
        logger.info("sending message: {}", message);
        rabbitTemplate.convertAndSend(QueueConfig.eventQueue, message);
    }

}
