/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 08-Feb-2017
 */
package com.capillary.expectation.state;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capillary.expectation.data.dao.ExpectationDataDao;
import com.capillary.expectation.data.entity.ExpectationDataEntity;
import com.capillary.expectation.event.EvaluationContext;
import com.capillary.expectation.event.model.BaseModel;
import com.capillary.expectation.rule.ExpectationInfo;
import com.capillary.expectation.rule.impl.ExpectationImpl;
import com.capillary.expectation.state.definition.Event;
import com.capillary.expectation.state.definition.StateSpace;

/**
 * @author aditya
 *
 */
@Service
public class StateMachine {

    private static final Logger logger = LoggerFactory.getLogger(StateMachine.class);
    private static final int ONE_MINUTE = 10 * 1000;//TODO - change back to 1 minute

    @Autowired
    private ExpectationDataDao expectationDataDao;

    @Autowired
    private ConfigManager configManager;

    private Timer expectationCheckerCron;

    @PostConstruct
    public void init() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runCron();
            }
        };
        expectationCheckerCron = new Timer("expectationCron", true);
        expectationCheckerCron.scheduleAtFixedRate(timerTask, 10 * 1000, ONE_MINUTE);
    }

    @SuppressWarnings("rawtypes")
    public void processInput(EvaluationContext evalContext) {
        logger.info("processing input");
        //check if any expectation exists on given event
        String merchantId = evalContext.getMerchantId();
        List<ExpectationInfo> expectations = configManager.getExpectationsForMerchant(merchantId);
        StateSpace tenantStateSpace = configManager.getTenantStateSpace(merchantId);

        BaseModel model = evalContext.getModel();
        Event event = Event.builder().entityModified(model.getName()).modification(model.getStatus()).build();

        for (ExpectationInfo expectation : expectations) {
            processExpectation(evalContext, tenantStateSpace, event, expectation);
        }
        logger.info("completed processing input: {}", model);
    }

    public void runCron() {
        //Run cron every minute
        //Search through all expectations that have not been met
        logger.info("running checker for failed expectations");
        List<ExpectationDataEntity> lapsedExpectations = expectationDataDao
                .findLapsedExpectations(new Date(System.currentTimeMillis() - ONE_MINUTE), new Date());
        lapsedExpectations.forEach((exp) -> configManager.getExpectation(exp.getExpectationUid()).executeActions(exp));
    }

    /**
     * @param evalContext 
     * @param tenantStateSpace
     * @param event
     * @param expectation
     */
    private void processExpectation(EvaluationContext evalContext, StateSpace tenantStateSpace, Event event,
            ExpectationInfo expectation) {
        if (tenantStateSpace.isEventRelevant(expectation.getInitialState(), event)) {
            //figure out actual id of initial state in multi state space
            Date expectedDateTime =
                    new Date(evalContext.getReceivedDate().getTime() + expectation.getExpectedTimeInSeconds() * 1000);
            ExpectationDataEntity expectationDataEntity = ExpectationDataEntity
                    .builder()
                    .tenantId(evalContext.getMerchantId())
                    .expectationUid(expectation.getId())
                    .entityId("" + evalContext.getModel().getId())//resolve the id based on state
                    .initialDateTime(evalContext.getReceivedDate())
                    .expectedDateTime(expectedDateTime)
                    .receivedDateTime(evalContext.getReceivedDate())
                    .build();

            expectationDataDao.save(expectationDataEntity);
            logger.info("inserted into db");
        }
        if (tenantStateSpace.isEventRelevant(expectation.getFinalState(), event)) {
            //TODO - figure out how to relate id's of different entity types

            //Load the relevant state of the entity
            ExpectationDataEntity expectationData = expectationDataDao.findByExpectationAndEntityId(
                    evalContext.getMerchantId(), expectation.getId(), evalContext.getModel().getId());

            //process transitions - for multi state entities

            //Update DB with new states
            expectationDataDao.updateFinalState(expectationData.getUid(), evalContext.getReceivedDate());

            //Put success actions here
        }
    }
}
