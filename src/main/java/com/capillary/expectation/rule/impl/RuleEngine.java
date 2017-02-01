/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 01-Feb-2017
 */
package com.capillary.expectation.rule.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capillary.expectation.data.mongo.MongoBaseDao;
import com.capillary.expectation.event.EvaluationContext;
import com.capillary.expectation.rule.api.Action;
import com.capillary.expectation.rule.api.Operand;
import com.capillary.expectation.rule.api.Rule;
import com.capillary.expectation.rule.impl.action.LoggingAction;
import com.capillary.expectation.rule.impl.action.SaveToMongoAction;
import com.capillary.expectation.rule.impl.operators.ReturnPropertyOperator;

/**
 * @author aditya
 *
 */
@Component
public class RuleEngine {
    private static final Logger logger = LoggerFactory.getLogger(RuleEngine.class);
    
    @Autowired
    private MongoBaseDao baseDao;

    private List<Rule> ruleList = new ArrayList<>();

    @PostConstruct
    public void init() {
        //Populate rule list
        logger.info("populating dummy rule data");
        List<Operand> operands = new ArrayList<>();
        operands.add(new OperandImpl("Order", "Order", "P"));
        List<Action> actions = new ArrayList<>();
        actions.add(new LoggingAction());
        RuleImpl ruleImpl = new RuleImpl(null, Rule.EVENT, operands, new ReturnPropertyOperator(), 10, actions);
        ruleList.add(ruleImpl);
        
        List<Operand> operands2 = new ArrayList<>();
        operands2.add(new OperandImpl("Product", "Product", "Updated"));
        List<Action> actions2 = new ArrayList<>();
        actions2.add(new SaveToMongoAction(baseDao));
        RuleImpl ruleImpl2 = new RuleImpl(null, Rule.EVENT, operands2, new ReturnPropertyOperator(), 10, actions2);
        ruleList.add(ruleImpl2);
    }

    //TODO - add method to evaluate rules based on cron
    public void evaluateRulesOnEvent(EvaluationContext context) {
        List<Rule> selectMatchingRules = selectMatchingRules(context);
        logger.info("rules matched: {}", selectMatchingRules.size());
        for (Rule rule : selectMatchingRules) {
            executeRule(rule, context);
        }
    }

    private List<Rule> selectMatchingRules(EvaluationContext context) {
        //Load all rules
        //Match operand definition and return set of matching rules
        List<Rule> collect = ruleList
                .stream()
                .filter((r) -> Rule.EVENT.equals(r.getTrigger()))
                .filter((r) -> r.getMerchantId() == null || context.getMerchantId().equals(r.getMerchantId()))
                .filter((r) -> operandsMatch(r, context))
                .collect(Collectors.toList());
        return collect;
    }

    private boolean operandsMatch(Rule r, EvaluationContext context) {
        for (Operand o : r.getOperands()) {
            if (o.getTypeName().equals(context.getModel().getName())) {
                return true;
            }
        }
        return false;
    }

    private void executeRule(Rule rule, EvaluationContext context) {
        //Load if object exists in rules collection
        double operate = rule.getOperator().operate();
        List<Action> actions = rule.getActions(operate > rule.getThreshold());
        //Execute actions
        for (Action action : actions) {
            action.execute(context);
        }
    }
}
