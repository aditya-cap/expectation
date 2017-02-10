/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 10-Feb-2017
 */
package com.capillary.expectation.data.entity;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author aditya
 *
 */
@JsonInclude(value = Include.NON_EMPTY)
public class BaseMongoEntity {

    @Id
    protected String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
