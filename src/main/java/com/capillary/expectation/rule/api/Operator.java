/**
 * This code is intellectual property of Capillary Technologies. 
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 01-Feb-2017
 */
package com.capillary.expectation.rule.api;

/**
 * The implementation of the operator
 * @author aditya
 *
 */
public interface Operator {
    
    /**
     * Perform operation given context.
     * Should be stateless
     *TODO - decide params
     * @return
     */
    public double operate();

}
