package com.capillary.expectation.data.mongo;

import com.mongodb.MongoClient;

/**
 * 
 * @author aditya
 *
 */
public interface MongoDataSourceManager {

    MongoClient getMongoClient();
}
