/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 08-Feb-2017
 */
package com.capillary.expectation.state;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.capillary.expectation.rule.ExpectationInfo;
import com.capillary.expectation.rule.api.Action;
import com.capillary.expectation.rule.impl.ExpectationImpl;
import com.capillary.expectation.rule.impl.action.ActionFactory;
import com.capillary.expectation.state.definition.StateSpace;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author aditya
 *
 */
@Component
public class ConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    private static final String GLOBAL = "GLOBAL";//to be used for global expectations and transitions

    @Autowired
    private ActionFactory actionFactory;

    private Map<String, ExpectationImpl> expectationImplMap = new HashMap<>();
    private Map<String, List<ExpectationInfo>> merchantExpectationsMap = new HashMap<>();
    private Map<String, StateSpace> tenantStateMap = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        //Read from file or db and populate state map
        String stateDefinition = new String(FileCopyUtils.copyToByteArray(new File("src/main/resources/states.json")));

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Map<String, StateSpace>> typeRef = new TypeReference<Map<String, StateSpace>>() {
        };
        tenantStateMap = mapper.readValue(stateDefinition, typeRef);
        tenantStateMap.values().forEach((s) -> s.init());
        logger.info("completed loading states");

        loadExpectations();
    }

    private void loadExpectations() throws IOException {
        //TODO read from DB
        String expectationDefinition =
                new String(FileCopyUtils.copyToByteArray(new File("src/main/resources/expectations.json")));
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<ExpectationInfo>> typeRef = new TypeReference<List<ExpectationInfo>>() {
        };
        List<ExpectationInfo> expectationsList = mapper.readValue(expectationDefinition, typeRef);
        expectationsList.forEach(e -> initializeExpectation(e));
        logger.info("completed loading expectations");
    }

    private void initializeExpectation(ExpectationInfo exp) {
        List<Map<String, Object>> failureActionInfos = exp.getFailureActions();
        List<Action> failureActions = new ArrayList<>();
        for (Map<String, Object> actionInfo : failureActionInfos) {
            Action action = actionFactory.getAction(actionInfo);
            if (action != null) {
                failureActions.add(action);
            }
        }
        ExpectationImpl expectationImpl = new ExpectationImpl(exp, failureActions);
        merchantExpectationsMap.computeIfAbsent(exp.getTenantId(), (k) -> new ArrayList<ExpectationInfo>()).add(exp);
        expectationImplMap.put(exp.getId(), expectationImpl);
    }

    public StateSpace getTenantStateSpace(String tenantId) {
        //return tenantStateMap.get(tenantId);
        return tenantStateMap.get(GLOBAL);
    }

    public ExpectationImpl getExpectation(String expectationUid) {
        return expectationImplMap.get(expectationUid);
    }

    public List<ExpectationInfo> getExpectationsForMerchant(String tenantId) {
        List<ExpectationInfo> expectations = new ArrayList<>();
        expectations.addAll(merchantExpectationsMap.getOrDefault(tenantId, new ArrayList<>()));
        expectations.addAll(merchantExpectationsMap.getOrDefault(GLOBAL, new ArrayList<>()));
        return expectations;
    }

}
