package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Nationalized;

/**
 * Mapping for DB view
 */
@Entity
@Immutable
@Table(name = "VIEW_COURSE_GRADE_SPLITS")
public class ViewCourseGradeSplit {
    @Id
    @Column(name = "COURSE_ID", nullable = false)
    private Long courseId;

    @Nationalized
    @Column(name = "COURSE_NAME", nullable = false, length = 20)
    private String courseName;

    @Column(name = "COURSE_YEAR", nullable = false, length = 20)
    private String courseYear;

    @Column(name = "AVERAGE_MARK")
    private Double averageMark;

    @Column(name = "FIRST")
    private Long first;

    @Column(name = "TWO_ONE")
    private Long twoOne;

    @Column(name = "TWO_TWO")
    private Long twoTwo;

    @Column(name = "THIRD")
    private Long third;

    @Column(name = "FAIL")
    private Long fail;

    public Long getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseYear() {
        return courseYear;
    }

    public Double getAverageMark() {
        return averageMark;
    }

    public Long getFirst() {
        return first;
    }

    public Long getTwoOne() {
        return twoOne;
    }

    public Long getTwoTwo() {
        return twoTwo;
    }

    public Long getThird() {
        return third;
    }

    public Long getFail() {
        return fail;
    }

    protected ViewCourseGradeSplit() {
    }
}