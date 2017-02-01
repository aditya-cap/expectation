package com.capillary.expectation;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.capillary.expectation.queues.EventPublisher;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class ExpectationServiceRunner {
    //TODO - add shutdown hook
    public static void main(String[] args) throws BeansException, Exception {
        ConfigurableApplicationContext context = SpringApplication.run(ExpectationServiceRunner.class, args);
        context.getBean(EventPublisher.class).send("Hello from RabbitMQ! " + System.currentTimeMillis());
        Thread.sleep(2000);
        context.close();
    }
}
