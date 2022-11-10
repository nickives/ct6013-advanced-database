package uk.edu.glos.s1909632.ct6013.backend.ents.oracle;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StudentModuleEntityId implements Serializable {
    private static final long serialVersionUID = 4964577426287217365L;
    @Column(name = "STUDENT_ID", nullable = false)
    private Long studentId;

    @Column(name = "MODULE_ID", nullable = false)
    private Long moduleId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentModuleEntityId entity = (StudentModuleEntityId) o;
        return Objects.equals(this.studentId, entity.studentId) &&
                Objects.equals(this.moduleId, entity.moduleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, moduleId);
    }

}