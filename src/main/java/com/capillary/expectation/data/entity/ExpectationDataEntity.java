/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 09-Feb-2017
 */
package com.capillary.expectation.data.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "expectation_data")
public @Data @NoArgsConstructor class ExpectationDataEntity extends BaseMongoEntity {

    private String tenantId;
    private String expectationUid;
    //TODO - add entity here
    private String entityId;
    private Date receivedDateTime;

    private Date initialDateTime;
    private Date expectedDateTime;
    private Date finalDateTime;

    @Builder
    public ExpectationDataEntity(String tenantId, String expectationUid, String entityId, Date receivedDateTime,
            Date initialDateTime, Date expectedDateTime) {
        super();
        this.tenantId = tenantId;
        this.expectationUid = expectationUid;
        this.entityId = entityId;
        this.receivedDateTime = receivedDateTime;
        this.initialDateTime = initialDateTime;
        this.expectedDateTime = expectedDateTime;
    }

    //TODO - add the conditions and previous states here

}
