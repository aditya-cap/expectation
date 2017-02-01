/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 01-Feb-2017
 */
package com.capillary.expectation.rule.impl;

import java.util.List;

import com.capillary.expectation.rule.api.Action;
import com.capillary.expectation.rule.api.Operand;
import com.capillary.expectation.rule.api.Operator;
import com.capillary.expectation.rule.api.Rule;

/**
 * @author aditya
 *
 */
public class RuleImpl implements Rule {

    private String merchantId;
    private String trigger;
    private List<Operand> operands;
    private Operator operator;
    private double threshold;
    private List<Action> actions;

    /**
     * @param merchantId
     * @param trigger
     * @param operands
     * @param operator
     * @param threshold
     * @param actions
     */
    public RuleImpl(String merchantId, String trigger, List<Operand> operands, Operator operator, double threshold,
            List<Action> actions) {
        super();
        this.merchantId = merchantId;
        this.trigger = trigger;
        this.operands = operands;
        this.operator = operator;
        this.threshold = threshold;
        this.actions = actions;
    }

    @Override
    public String getMerchantId() {
        return merchantId;
    }

    @Override
    public String getTrigger() {
        return trigger;
    }

    @Override
    public List<Operand> getOperands() {
        return operands;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public double getThreshold() {
        return threshold;
    }

    @Override
    public List<Action> getActions(boolean caseValue) {
        return actions;
    }

}
