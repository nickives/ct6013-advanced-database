package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo;

public enum MongoCollections {
    LECTURER("lecturer"),
    COURSE("course"),
    STUDENT("student");

    private final String collectionName;
    MongoCollections(String collectionName) {
        this.collectionName = collectionName;
    }

    public String toString() {
        return collectionName;
    }
}
