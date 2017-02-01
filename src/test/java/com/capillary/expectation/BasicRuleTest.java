package com.capillary.expectation;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import com.capillary.expectation.data.mongo.MongoBaseDao;
import com.capillary.expectation.queues.EventConsumer;
import com.capillary.expectation.queues.EventPublisher;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicRuleTest {

    @Autowired
    private EventPublisher producer;

    @Autowired
    private EventConsumer consumer;

    @Autowired
    private MongoBaseDao mongoBaseDao;

    @Test
    public void orderTest() throws Exception {
        String file = new String(FileCopyUtils.copyToByteArray(new File("src/test/resources/events/order1.json")));
        producer.send(file);
        Thread.sleep(3000);
        System.out.println(consumer.getReceivedCount());
        Assert.assertThat(consumer.getReceivedCount(), Matchers.greaterThan(1));
    }

    @Test
    public void productTest() throws Exception {
        String file =
                new String(FileCopyUtils.copyToByteArray(new File("src/test/resources/events/product_create.json")));
        producer.send(file);
        Thread.sleep(3000);
        List<Map> findAll = mongoBaseDao.getTemplate().findAll(Map.class, "merchant");
        Assert.assertEquals(findAll.size(), 1);
    }
}
