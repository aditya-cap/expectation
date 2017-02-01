/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 01-Feb-2017
 */
package com.capillary.expectation.rule.api;

import java.util.List;

/**
 * The meta definition of the rule. Created at the time when config is modified
 * @author aditya
 */
public interface Rule {

    //Add cron also here
    public String EVENT = "Event";

    String getMerchantId();

    //Check if enum should be used here
    String getTrigger();

    List<Operand> getOperands();

    Operator getOperator();

    double getThreshold();

    List<Action> getActions(boolean caseValue);
}
