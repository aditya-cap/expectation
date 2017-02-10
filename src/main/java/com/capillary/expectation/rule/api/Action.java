/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 01-Feb-2017
 */
package com.capillary.expectation.rule.api;

import com.capillary.expectation.data.entity.ExpectationDataEntity;
import com.capillary.expectation.event.EvaluationContext;

/**
 * @author aditya
 *
 */
public interface Action {

    //TODO - add rule context here
    void execute(EvaluationContext context);

    void execute(ExpectationDataEntity expectationData);

}
