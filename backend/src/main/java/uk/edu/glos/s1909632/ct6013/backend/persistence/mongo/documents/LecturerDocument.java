package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents;

import org.bson.codecs.pojo.annotations.*;
import org.bson.types.ObjectId;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class LecturerDocument {
    @BsonId
    private ObjectId id;

    @BsonProperty
    private String name;

    @BsonProperty
    private Set<ObjectId> moduleIds;

    public LecturerDocument() {}

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

    public Set<ObjectId> getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(
            Set<ObjectId> moduleIds) {
        this.moduleIds = moduleIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LecturerDocument that = (LecturerDocument) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Optional.ofNullable(getId())
                .map(ObjectId::hashCode)
                .orElse(super.hashCode());
    }
}

