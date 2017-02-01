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

import com.capillary.expectation.data.mongo.MongoBaseDao;
import com.capillary.expectation.event.EvaluationContext;
import com.capillary.expectation.rule.api.Action;

/**
 * @author aditya
 *
 */
public class SaveToMongoAction implements Action {

    private static final Logger logger = LoggerFactory.getLogger(SaveToMongoAction.class);

    private MongoBaseDao baseDao;

    /**
     * @param baseDao
     */
    public SaveToMongoAction(MongoBaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public void execute(EvaluationContext context) {
        BasicBSONObject obj = new BasicBSONObject();
        obj.put("merchantId", context.getMerchantId());
        baseDao.getTemplate().insert(obj, "merchant");
        logger.info("inserted into db");
    }

}
