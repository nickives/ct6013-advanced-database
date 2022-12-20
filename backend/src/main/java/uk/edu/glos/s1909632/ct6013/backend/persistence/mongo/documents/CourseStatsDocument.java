package uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.documents;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public final class CourseStatsDocument {
    public final static class CourseStatsDocumentId {
        @BsonProperty
        private ObjectId courseId;

        @BsonProperty
        private String courseName;

        @BsonProperty
        private String courseYear;

        public CourseStatsDocumentId() {}

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

        public String getCourseYear() {
            return courseYear;
        }

        public void setCourseYear(String courseYear) {
            this.courseYear = courseYear;
        }
    }

    @BsonId
    private CourseStatsDocumentId id;

    @BsonProperty
    private Double averageMark;

    @BsonProperty
    private Long first;

    @BsonProperty
    private Long twoOne;

    @BsonProperty
    private Long twoTwo;

    @BsonProperty
    private Long third;

    @BsonProperty
    private Long fail;

    public CourseStatsDocument() {}

    public CourseStatsDocumentId getId() {
        return id;
    }

    public void setId(
            CourseStatsDocumentId id) {
        this.id = id;
    }

    public Double getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(Double averageMark) {
        this.averageMark = averageMark;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getTwoOne() {
        return twoOne;
    }

    public void setTwoOne(Long twoOne) {
        this.twoOne = twoOne;
    }

    public Long getTwoTwo() {
        return twoTwo;
    }

    public void setTwoTwo(Long twoTwo) {
        this.twoTwo = twoTwo;
    }

    public Long getThird() {
        return third;
    }

    public void setThird(Long third) {
        this.third = third;
    }

    public Long getFail() {
        return fail;
    }

    public void setFail(Long fail) {
        this.fail = fail;
    }
}
