package com.capillary.expectation.data.mongo;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@Profile("production")
@ConfigurationProperties(prefix = "spring.mongo.datasource")
public class MongoDbConfiguration implements MongoDataSourceManager {

    private static final Logger logger = LoggerFactory.getLogger(MongoDbConfiguration.class);

    //@Value("${spring.mongo.datasource.host}")
    private String host;

    //@Value("${spring.mongo.datasource.port}")
    private int port;

    //@Value("${spring.mongo.datasource.database}")
    private String database;

    //@Value("${spring.mongo.datasource.username}")
    private String username;

    //@Value("${spring.mongo.datasource.password}")
    private String password;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public MongoClient getMongoClient() {
        try {
            logger.debug("mondodb username {} ", host);

            // Set credentials
            MongoCredential credential = MongoCredential.createCredential(username, database, password.toCharArray());
            ServerAddress serverAddress = new ServerAddress(host, port);

            // Mongo Client
            MongoClient mongoClient = new MongoClient(serverAddress, Arrays.asList(credential));

            return mongoClient;
        } catch (Exception e) {
            logger.error("could not initialize mongo client", e);
            throw new RuntimeException(e);
        }
    }
}
