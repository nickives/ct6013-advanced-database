package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.ents;

import org.bson.codecs.pojo.annotations.*;
import org.bson.types.ObjectId;

import java.util.List;

public class LecturerMongoEntity {
    @BsonId
    private ObjectId id;

    @BsonProperty
    private String name;

    private List<ModuleMongo> modules;

    public LecturerMongoEntity() {}

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

    public List<ModuleMongo> getModules() {
        return modules;
    }

    public void setModules(
            List<ModuleMongo> modules) {
        this.modules = modules;
    }
}

