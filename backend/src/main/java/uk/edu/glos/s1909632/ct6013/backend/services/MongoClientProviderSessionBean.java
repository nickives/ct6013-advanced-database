package uk.edu.glos.s1909632.ct6013.backend.services;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.*;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Singleton(name = "MongoClientProviderSessionEJB")
public class MongoClientProviderSessionBean {

    @EJB
    ConfigurationBean config;

    private MongoClient mongoClient = null;

    public MongoClientProviderSessionBean() {
    }

    public MongoClient getMongoClient() { return mongoClient; }

    public MongoDatabase getDatabase() {
        return mongoClient.getDatabase(config.getMongoConfig().getDatabaseName());
    }

    @PostConstruct
    public void init() {
        ConnectionString connectionString = new ConnectionString(config.getMongoConfig().getConnectionString());
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();
        mongoClient = MongoClients.create(clientSettings);
    }
}
