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
@Table(name = "VIEW_MODULE_AGGREGATE_RESULTS")
public class ViewModuleAggregateResult {
    @Id
    @Nationalized
    @Column(name = "MODULE_NAME", nullable = false, length = 20)
    private String moduleName;

    @Column(name = "COURSE_ID", nullable = false)
    private Long courseId;

    @Column(name = "MODULE_ID", nullable = false)
    private Long moduleId;

    @Column(name = "AVERAGE_MARK")
    private Double averageMark;

    @Column(name = "NUMBER_OF_STUDENTS")
    private Long numberOfStudents;

    public String getModuleName() {
        return moduleName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public Double getAverageMark() {
        return averageMark;
    }

    public Long getNumberOfStudents() {
        return numberOfStudents;
    }

    protected ViewModuleAggregateResult() {
    }
}