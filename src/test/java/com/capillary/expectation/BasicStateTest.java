package com.capillary.expectation;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.bson.BasicBSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import com.capillary.expectation.data.dao.MongoSampleDao;
import com.capillary.expectation.data.entity.ExpectationDataEntity;
import com.capillary.expectation.queues.EventPublisher;
import com.capillary.expectation.queues.QueueConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicStateTest {

    private static final Logger logger = LoggerFactory.getLogger(BasicStateTest.class);

    @Autowired
    private EventPublisher producer;

    @Autowired
    private MongoSampleDao mongoBaseDao;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Before
    public void setup() {
        logger.info("purging events queue for test");
        rabbitAdmin.purgeQueue(QueueConfig.eventQueue, true);
    }
    
    @SuppressWarnings("rawtypes")
//    @Test
    public void dbTest() throws Exception {
        BasicBSONObject basicBSONObject = new BasicBSONObject();
        basicBSONObject.put("a", 1);
        basicBSONObject.put("b", "2");
        mongoBaseDao.getTemplate().save(basicBSONObject, "test_data");
        
        Query query = new Query(Criteria.where("b").is(1));
        Map findOne = mongoBaseDao.getTemplate().findOne(query, Map.class, "test_data");
        logger.info("something:{}",findOne);
    }

    @SuppressWarnings("rawtypes")
//    @Test
    public void productTest() throws Exception {
        logger.info("invoking product test");
        String file = new String(FileCopyUtils.copyToByteArray(new File("src/test/resources/events/order1.json")));
        producer.send(file);
        Thread.sleep(5000);
        String file2 =
                new String(FileCopyUtils.copyToByteArray(new File("src/test/resources/events/order_pos_updated.json")));
        producer.send(file2);
        Thread.sleep(30000);

        //Load the relevant state of the entity
        CriteriaDefinition criteria = Criteria.where("entityId").is("4906865");
        Query query = Query.query(criteria);

        ExpectationDataEntity findOne = mongoBaseDao.getTemplate().findOne(query, ExpectationDataEntity.class, "expectation_data");
        
        
        List<Map> findAll = mongoBaseDao.getTemplate().findAll(Map.class, "expectation_data");

        Assert.assertNotNull(findOne);
//        Assert.assertNotNull(findOne.get("finalDateTime"));
        Assert.assertNotNull(findOne.getFinalDateTime());
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void failedExpectationTest() throws Exception {
        logger.info("invoking product test");
        String file = new String(FileCopyUtils.copyToByteArray(new File("src/test/resources/events/order1.json")));
        producer.send(file);
        for (int i = 0; i < 100; i++) {
            logger.info("continuing to sleep");
            Thread.sleep(1000);
        }

        logger.info("checking in failed collection");
        //Load the relevant state of the entity
        CriteriaDefinition criteria = Criteria.where("entityId").is("4906865");
        Query query = Query.query(criteria);

        Map findOne = mongoBaseDao.getTemplate().findOne(query, Map.class, "failedExpectations");
        
        Assert.assertNotNull(findOne);
//        Assert.assertNotNull(findOne.get("finalDateTime"));
//        Assert.assertNotNull(findOne.getFinalDateTime());
    }
}
