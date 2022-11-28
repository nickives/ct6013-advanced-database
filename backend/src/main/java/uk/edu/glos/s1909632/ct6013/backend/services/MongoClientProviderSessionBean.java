package uk.edu.glos.s1909632.ct6013.backend.services;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.*;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.MongoCollections;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.CourseDocument;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents.LecturerDocument;

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

    private void createCourseSchema() {
        MongoCollection<CourseDocument> collection = getDatabase().getCollection(
                MongoCollections.COURSE.toString(), CourseDocument.class);
        IndexOptions indexOptions = new IndexOptions().unique(true);
        collection.createIndex(Indexes.ascending("name"), indexOptions);
    }

    private void createSchema() {
        createCourseSchema();
        getDatabase().getCollection(MongoCollections.LECTURER.toString(),
                                    LecturerDocument.class);
        getDatabase().getCollection(MongoCollections.STUDENT.toString());
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
        createSchema();
    }

    @PreDestroy
    public void destroy() {
        mongoClient.close();
    }
}
