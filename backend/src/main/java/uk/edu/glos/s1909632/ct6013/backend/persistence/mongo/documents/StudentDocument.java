package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import uk.edu.glos.s1909632.ct6013.backend.Grade;

import java.util.HashSet;
import java.util.Set;

public final class StudentDocument {
    @BsonId
    private ObjectId id;

    @BsonProperty
    private String firstName;

    @BsonProperty
    private String lastName;

    @BsonProperty
    private ObjectId courseId;

    @BsonProperty
    private String courseName;

    @BsonProperty
    private Set<StudentModuleDocument> modules;

    @BsonProperty
    private Grade grade;

    public StudentDocument() {}

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ObjectId getCourseId() {
        return courseId;
    }

    public void setCourseId(
            ObjectId courseId) {
        this.courseId = courseId;
    }

    @NotNull
    public Set<StudentModuleDocument> getModules() {
        if (modules == null) modules = new HashSet<>();
        return modules;
    }

    public void setModules(
            Set<StudentModuleDocument> modules) {
        this.modules = modules;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
