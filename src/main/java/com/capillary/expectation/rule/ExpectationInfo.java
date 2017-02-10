/**
 * This code is intellectual property of Capillary Technologies. 
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 08-Feb-2017
 */
package com.capillary.expectation.rule;

import java.util.List;
import java.util.Map;

import com.capillary.expectation.state.definition.State;

import lombok.Data;

/**
 * @author aditya
 *
 */
public @Data class ExpectationInfo {

    private String tenantId;
    private String id;
    //TODO need to work off 1 entity
    private State initialState;
    private State finalState;
    private long expectedTimeInSeconds;
    private List<Map<String, Object>> failureActions;

}
