package com.capillary.expectation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.capillary.expectation.queues.EventConsumer;
import com.capillary.expectation.queues.EventPublisher;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicQueueTest {

    @Autowired
    private EventPublisher producer;

    @Autowired
    private EventConsumer consumer;

    @Test
    public void test() throws Exception {
        producer.send("Hello from RabbitMQ!");
        producer.send("Boo");
        Thread.sleep(20000);
        System.out.println(consumer.getReceivedCount());
        Assert.assertEquals(2, consumer.getReceivedCount());
    }

}
