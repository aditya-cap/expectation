package com.capillary.expectation.queues;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capillary.expectation.event.EvaluationContext;
import com.capillary.expectation.event.model.BaseModel;
import com.capillary.expectation.event.model.ModelResolver;
import com.capillary.expectation.rule.impl.RuleEngine;
import com.capillary.expectation.state.StateMachine;

@Component
public class EventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    public static final AtomicInteger MESSAGE_COUNT = new AtomicInteger();

    @Autowired
    private ModelResolver modelResolver;

    @Autowired
    private RuleEngine ruleEngine;
    
    @Autowired
    private StateMachine stateMachine;


    @PostConstruct
    private void init() {
        logger.info("accepting messages now");
    }

    public void receiveMessage(Object message) {
        logger.trace("received message: {}", message);
        MESSAGE_COUNT.incrementAndGet();

        BaseModel resolveModel = null;
        try {
            resolveModel = modelResolver.resolveModel(message);
            EvaluationContext evaluationContext = new EvaluationContext(new Date(), message, resolveModel);
            //ruleEngine.evaluateRulesOnEvent(evaluationContext);
            stateMachine.processInput(evaluationContext);
            logger.info("completed processing event");
        } catch (Exception e) {
            logger.warn("could not process message: " + message, e);
        }
    }

    public int getReceivedCount() {
        int receivedCount = MESSAGE_COUNT.get();
        logger.info("num received messages: {}", receivedCount);
        return receivedCount;
    }
}
