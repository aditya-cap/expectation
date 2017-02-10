/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 01-Feb-2017
 */
package com.capillary.expectation.rule.impl.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.expectation.data.entity.ExpectationDataEntity;
import com.capillary.expectation.event.EvaluationContext;
import com.capillary.expectation.rule.api.Action;

/**
 * @author aditya
 *
 */
public class LoggingAction implements Action {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAction.class);

    @Override
    public void execute(EvaluationContext context) {
        logger.info("all hail! the event has executed: {}", context);
    }

    @Override
    public void execute(ExpectationDataEntity context) {
        logger.info("failed expectation: {}", context);
    }

}
