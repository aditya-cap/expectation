/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 09-Feb-2017
 */
package com.capillary.expectation.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.capillary.expectation.data.entity.ExpectationDataEntity;
import com.capillary.expectation.data.mongo.MongoDataSourceManager;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

/**
 * @author aditya
 *
 */
@Service
public class ExpectationDataDao extends BaseMongoDaoImpl<ExpectationDataEntity> {

    public static final String EXPECTATION_DATA = "expectation_data";

    public static final String UID = "uid";
    public static final String TENANT_ID = "tenantId";
    public static final String EXPECTATION_UID = "expectationUid";
    public static final String ENTITY_ID = "entityId";
    public static final String EXPECTED_DATE_TIME = "expectedDateTime";
    public static final String FINAL_DATE_TIME = "finalDateTime";

    @Autowired
    private MongoDataSourceManager mongoDataSourceManager;

    @SuppressWarnings("rawtypes")
    @Override
    public Class getEntityClass() {
        return ExpectationDataEntity.class;
    }

    @Override
    public MongoDataSourceManager getDataSource() {
        return mongoDataSourceManager;
    }

    public ExpectationDataEntity findByExpectationAndEntityId(String tenantId, String expectationId, Object entityId) {
        CriteriaDefinition criteria = Criteria
                .where(TENANT_ID)
                .is(tenantId)
                .and(EXPECTATION_UID)
                .is(expectationId)
                .and(ENTITY_ID)
                .is("" + entityId);
        Query query = Query.query(criteria);
        return getTemplate().findOne(query, ExpectationDataEntity.class);
    }

    public List<ExpectationDataEntity> findLapsedExpectations(Date previousRunDate, Date timeToCheck) {
        Criteria diffCriteria = new Criteria() {
            @Override
            public DBObject getCriteriaObject() {
                DBObject obj = new BasicDBObject();
                obj.put("$where", "this." + FINAL_DATE_TIME + "> this." + EXPECTED_DATE_TIME);
                return obj;
            }
        };

        Criteria[] orCriteria = { Criteria.where(FINAL_DATE_TIME).is(null),
                Criteria.where(FINAL_DATE_TIME).gt(timeToCheck), diffCriteria };
        CriteriaDefinition criteria = Criteria
                .where(EXPECTED_DATE_TIME)
                .lt(timeToCheck)
                .andOperator(Criteria.where(EXPECTED_DATE_TIME).gte(previousRunDate))
                .orOperator(orCriteria);
        
        //TODO - check efficiency - might have to update the diff at the time of updates
        Query query = Query.query(criteria);
        return getTemplate().find(query, ExpectationDataEntity.class);
    }

    public void updateFinalState(String uid, Date finalDateTime) {
        //TODO - make this an upsert instead to account for multi threading
        CriteriaDefinition criteria = Criteria.where(UID).is(uid);
        Query query = Query.query(criteria);

        Update update = new Update();
        update.set(FINAL_DATE_TIME, finalDateTime);
        WriteResult updateFirst = getTemplate().updateFirst(query, update, ExpectationDataEntity.class);
    }

}
