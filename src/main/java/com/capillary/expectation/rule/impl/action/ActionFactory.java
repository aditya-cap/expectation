/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 10-Feb-2017
 */
package com.capillary.expectation.rule.impl.action;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capillary.expectation.data.dao.MongoSampleDao;
import com.capillary.expectation.rule.api.Action;

/**
 * @author aditya
 *
 */
@Component
public class ActionFactory {

    private static final Logger logger = LoggerFactory.getLogger(ActionFactory.class);

    @Autowired
    private MongoSampleDao baseDao;

    public Action getAction(Map<String, Object> actionInfo) {
        String actionName = (String)(actionInfo.get("actionName"));
        switch (actionName) {
            case "saveToMongoAction":
                return new SaveToMongoAction(baseDao);
            case "loggingAction":
                return new LoggingAction();
            default:
                logger.warn("could not resolve action: {}", actionName);
                return null;
        }
    }
}
