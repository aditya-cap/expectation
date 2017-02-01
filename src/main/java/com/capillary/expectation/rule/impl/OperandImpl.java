/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 01-Feb-2017
 */
package com.capillary.expectation.rule.impl;

import com.capillary.expectation.rule.api.Operand;

/**
 * @author aditya
 *
 */
public class OperandImpl implements Operand {

    private String name;
    private String typeName;
    private String state;

    /**
     * @param name
     * @param typeName
     * @param state
     */
    public OperandImpl(String name, String typeName, String state) {
        super();
        this.name = name;
        this.typeName = typeName;
        this.state = state;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public String getState() {
        return state;
    }

}
