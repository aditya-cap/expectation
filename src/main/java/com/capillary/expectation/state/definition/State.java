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

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author aditya
 *
 */
@JsonInclude(value = Include.NON_EMPTY)
public @Data @NoArgsConstructor class State {

    public String entity;
    public String status;

    @Builder
    public State(String entity, String status) {
        super();
        this.entity = entity;
        this.status = status;
    }

    public boolean matches(String entityName, String entityStatus) {
        return entity.equals(entityName) && status.equals(entityStatus);
    }
}
