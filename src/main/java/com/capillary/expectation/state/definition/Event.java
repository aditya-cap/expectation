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
public @Data @NoArgsConstructor class Event {

    public String entityModified;
    public String modification;

    @Builder
    public Event(String entityModified, String modification) {
        super();
        this.entityModified = entityModified;
        this.modification = modification;
    }

}
