
package com.capillary.expectation.data.mongo;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;

@Profile("development")
@Configuration
@ConfigurationProperties(prefix = "spring.embedmongo.datasource")
public class EmbedMongoDbConfiguration implements MongoDataSourceManager {

    private static final Logger logger = LoggerFactory.getLogger(EmbedMongoDbConfiguration.class);

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

    private MongodProcess mongod;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*public EmbedMongoDbConfiguration() {
        super(KnownService.MERCURY_DB_MONGO_MASTER, "mercury");
    }*/

    @PostConstruct
    public void startEmbedMongoDb() {
        logger.info("attempting to start in memory mongo");
        MongodStarter starter = MongodStarter.getDefaultInstance();
        IMongodConfig mongodConfig;
        try {
            mongodConfig =
                    new MongodConfigBuilder().version(Version.Main.DEVELOPMENT).net(new Net(host, port, false)).build();

            MongodExecutable mongodExe = starter.prepare(mongodConfig);
            mongod = mongodExe.start();
            if (mongod.isProcessRunning()) {
                logger.debug("in memory mongodb started successfully: {}", mongodConfig.params());
            }
        } catch (UnknownHostException e) {
            logger.error("error while starting in memory mongo db", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("error while start in memory mongo db", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (mongod.isProcessRunning()) {
            mongod.stop();
        }
    }

    @PreDestroy
    private void stopEmbedMongoDb() {
        if (mongod != null) {
            mongod.stop();
        }
    }

    @Override
    public String toString() {
        return "MongoDbConfiguration [host="
               + host
               + ", port="
               + port
               + ", database="
               + database
               + ", username="
               + username
               + ", password="
               + password
               + "]";
    }

    @Override
    public MongoClient getMongoClient() {
        try {
            // Set credentials
            logger.info("fetching mongo client");
            MongoCredential credential = MongoCredential.createCredential(username, database, password.toCharArray());
            ServerAddress serverAddress = new ServerAddress(host, port);

            // Mongo Client
            MongoClient mongoClient = new MongoClient(serverAddress);

            return mongoClient;
        } catch (Exception e) {
            logger.error("error while fetching mongo client", e);
            throw new RuntimeException(e);
        }
    }
}
