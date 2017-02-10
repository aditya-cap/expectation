/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 10-Feb-2017
 */
package com.capillary.expectation.rule.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.expectation.data.entity.ExpectationDataEntity;
import com.capillary.expectation.rule.ExpectationInfo;
import com.capillary.expectation.rule.api.Action;

import lombok.Data;

/**
 * @author aditya
 *
 */
public @Data class ExpectationImpl {

    private static final Logger logger = LoggerFactory.getLogger(ExpectationImpl.class);

    private final ExpectationInfo expectationInfo;
    private final List<Action> actions;

    public void executeActions(ExpectationDataEntity expectationDataEntity) {
        logger.info("executing actions for expectation uid: {} entityId: {}", expectationInfo.getId(),
                expectationDataEntity.getEntityId());
        for (Action action : actions) {
            action.execute(expectationDataEntity);
        }
    }
    
}
