package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents;

import jakarta.persistence.*;

@Entity
@Table(name = "STUDENT_MODULE")
public class StudentModuleEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EmbeddedId
    private StudentModuleEntityId id;

    @MapsId("studentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "STUDENT_ID", nullable = false)
    private StudentEntity student;

    @MapsId("moduleId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MODULE_ID", nullable = false)
    private ModuleEntity module;

    @Column(name = "MARK")
    private Long mark;

    public StudentModuleEntityId getId() {
        return id;
    }

    public void setId(StudentModuleEntityId id) {
        this.id = id;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    public ModuleEntity getModule() {
        return module;
    }

    public void setModule(ModuleEntity module) {
        this.module = module;
    }

    public Long getMark() {
        return mark;
    }

    public void setMark(Long mark) {
        this.mark = mark;
    }

}