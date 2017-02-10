/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 01-Feb-2017
 */
package com.capillary.expectation.rule.impl.action;

import org.bson.BasicBSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capillary.expectation.data.dao.MongoSampleDao;
import com.capillary.expectation.data.entity.ExpectationDataEntity;
import com.capillary.expectation.event.EvaluationContext;
import com.capillary.expectation.rule.api.Action;

/**
 * @author aditya
 *
 */
public class SaveToMongoAction implements Action {

    private static final Logger logger = LoggerFactory.getLogger(SaveToMongoAction.class);

    private MongoSampleDao baseDao;

    public SaveToMongoAction(MongoSampleDao baseDao) {
        this.baseDao = baseDao;
    }
    
    public String getName() {
        return "saveToMongoAction";
    }

    @Override
    public void execute(EvaluationContext context) {
        BasicBSONObject obj = new BasicBSONObject();
        obj.put("merchantId", context.getMerchantId());
        baseDao.getTemplate().insert(obj, "merchant");
        logger.info("inserted into db");
    }

    @Override
    public void execute(ExpectationDataEntity expectationData) {
        BasicBSONObject obj = new BasicBSONObject();
        obj.put("expectationUid", expectationData.getTenantId());
        obj.put("entityId", expectationData.getEntityId());
        baseDao.getTemplate().insert(obj, "failedExpectations");
        logger.info("inserted into failed expectations");
    }

}
