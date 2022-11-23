package uk.edu.glos.s1909632.ct6013.backend.services;

import jakarta.ejb.Singleton;

@Singleton(name = "ConfigurationEJB")
public class ConfigurationBean {
    public static class MongoDBConfiguration {
        private final String connectionString;

        private final String databaseName;

        private MongoDBConfiguration(String connectionString, String databaseName) {
            this.connectionString = connectionString;
            this.databaseName = databaseName;
        }

        public String getConnectionString() {
            return connectionString;
        }

        public String getDatabaseName() {
            return databaseName;
        }
    }

    private final MongoDBConfiguration mongoDBConfiguration;

    public ConfigurationBean() {
        mongoDBConfiguration = new MongoDBConfiguration(
                System.getenv("MONGO_DB_URL"),
                System.getenv("MONGO_DB_NAME")
        );
    }

    public MongoDBConfiguration getMongoConfig() {
        return mongoDBConfiguration;
    }
}
