package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Objects;

public final class StudentModuleDocument {
    @BsonId
    private ObjectId id;
    @BsonProperty
    private ModuleDocument moduleDocument;

    @BsonProperty
    private Long mark;

    public StudentModuleDocument() {
    }

    public ModuleDocument getModuleDocument() {
        return moduleDocument;
    }

    public void setModuleDocument(
            ModuleDocument moduleDocument) {
        this.moduleDocument = moduleDocument;
    }

    public Long getMark() {
        return mark;
    }

    public void setMark(Long mark) {
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentModuleDocument that = (StudentModuleDocument) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
