/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 08-Feb-2017
 */
package com.capillary.expectation.state.definition;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author aditya
 *
 */
@JsonInclude(value = Include.NON_EMPTY)
public @Data class Transition {

    private State initialState;
    private State finalState;
    private Event event;
}
