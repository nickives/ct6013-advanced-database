package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.ents;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Set;

final public class CourseMongoEntity {
    @BsonId
    private ObjectId id;

    @BsonProperty
    private String name;

    @BsonProperty
    private Set<ObjectId> studentIds;

    @BsonProperty
    private Set<ModuleMongo> modules;

    public CourseMongoEntity() {}

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
