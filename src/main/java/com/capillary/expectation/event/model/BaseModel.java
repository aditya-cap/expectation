/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 01-Feb-2017
 */
package com.capillary.expectation.event.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Data that is passed as the payload of the event 
 * @author aditya
 */
public abstract class BaseModel<T> {

    private T id;
    private String name;
    private Map<String, Object> valueMap;

    public BaseModel(T id, String name) {
        super();
        this.id = id;
        this.name = name;
        valueMap = new HashMap<>();
    }

    public T getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMerchantId() {
        return (String) valueMap.get("MerchantId");
    }

    public String getStatus() {
        return (String) valueMap.get("status");
    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    @Override
    public String toString() {
        return "BaseModel ["
               + (id != null ? "id=" + id + ", " : "")
               + (name != null ? "name=" + name + ", " : "")
               + (getMerchantId() != null ? "getMerchantId()=" + getMerchantId() : "")
               + "]";
    }

}
