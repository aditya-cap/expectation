/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 31-Jan-2017
 */
package com.capillary.expectation.event;

import java.util.Date;

import com.capillary.expectation.event.model.BaseModel;

/**
 * @author aditya
 *
 */
@SuppressWarnings("rawtypes")
public class EvaluationContext {

    private Date receivedDate;
    private Object eventData;
    private BaseModel model;

    /**
     * @param receivedDate
     * @param eventData
     * @param model
     */
    public EvaluationContext(Date receivedDate, Object eventData, BaseModel model) {
        super();
        this.receivedDate = receivedDate;
        this.eventData = eventData;
        this.model = model;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public Object getEventData() {
        return eventData;
    }

    public BaseModel getModel() {
        return model;
    }

    public String getMerchantId() {
        return model.getMerchantId();
    }

    @Override
    public String toString() {
        return "EvaluationContext ["
               + (receivedDate != null ? "receivedDate=" + receivedDate + ", " : "")
               + (eventData != null ? "eventData=" + eventData + ", " : "")
               + (model != null ? "model=" + model : "")
               + "]";
    }

}
