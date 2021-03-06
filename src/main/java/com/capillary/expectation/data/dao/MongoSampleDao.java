/**
 * This code is intellectual property of Capillary Technologies.
 *
 * Copyright (c) (2017)
 * Created By aditya
 * Created On 01-Feb-2017
 */
package com.capillary.expectation.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.capillary.expectation.data.mongo.MongoDataSourceManager;

@Service
public class MongoSampleDao {

    @Autowired
    private MongoDataSourceManager mongoDataSourceManager;

    public MongoTemplate getTemplate() {
        return new MongoTemplate(mongoDataSourceManager.getMongoClient(), "expectation");
    }

}
