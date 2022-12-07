package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Objects;

final public class ModuleDocument {
    @BsonId
    private ObjectId id;

    @BsonProperty
    private String name;

    @BsonProperty
    private String code;

    @BsonProperty
    private String semester;

    @BsonProperty
    private Long catPoints;

    @BsonProperty
    private LecturerDocument lecturer;

    public ModuleDocument() {
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Long getCatPoints() {
        return catPoints;
    }

    public void setCatPoints(Long catPoints) {
        this.catPoints = catPoints;
    }

    public LecturerDocument getLecturer() {
        return lecturer;
    }

    public void setLecturer(LecturerDocument lecturer) {
        this.lecturer = lecturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleDocument that = (ModuleDocument) o;
        return getCode().equals(that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
