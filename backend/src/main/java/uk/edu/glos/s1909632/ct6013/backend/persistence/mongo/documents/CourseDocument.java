package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.*;

final public class CourseDocument {
    @BsonId
    private ObjectId id;

    @BsonProperty
    private String name;

    @BsonProperty
    private Set<ObjectId> studentIds;

    @BsonProperty
    private Set<ModuleDocument> modules;

    public CourseDocument() {}

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

    public Set<ObjectId> getStudentIds() {
        if (studentIds == null) studentIds = new HashSet<>();
        return studentIds;
    }

    public Set<ModuleDocument> getModules() {
        if (modules == null) modules = new HashSet<>();
        return modules;
    }

    public void setModules(
            Set<ModuleDocument> modules) {
        this.modules = modules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDocument that = (CourseDocument) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Optional.ofNullable(getId())
                .map(ObjectId::hashCode)
                .orElse(super.hashCode());
    }
}
