package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public final class ModuleStatsDocument {
    public final static class ModuleStatsDocumentId {
        @BsonProperty
        private ObjectId courseId;

        @BsonProperty
        private String courseName;

        @BsonProperty
        private ObjectId moduleId;

        @BsonProperty
        private String moduleName;

        public ModuleStatsDocumentId() {}

        public ObjectId getCourseId() {
            return courseId;
        }

        public void setCourseId(ObjectId courseId) {
            this.courseId = courseId;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public ObjectId getModuleId() {
            return moduleId;
        }

        public void setModuleId(ObjectId moduleId) {
            this.moduleId = moduleId;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }
    }

    @BsonId
    private ModuleStatsDocumentId id;

    @BsonProperty
    private Double averageMark;

    @BsonProperty
    private Long numberOfStudents;

    public ModuleStatsDocument() {}

    public ModuleStatsDocumentId getId() {
        return id;
    }

    public void setId(
            ModuleStatsDocumentId id) {
        this.id = id;
    }

    public Double getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(Double averageMark) {
        this.averageMark = averageMark;
    }

    public Long getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Long numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }
}
