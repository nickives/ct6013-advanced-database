package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "COURSE", indexes = {
        @Index(name = "COURSE_NAME", columnList = "NAME", unique = true)
})
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "NAME", nullable = false, length = 20)
    private String name;

    @ManyToMany
    @JoinTable(name = "STUDENT_COURSE",
            joinColumns = @JoinColumn(name = "COURSE_ID"),
            inverseJoinColumns = @JoinColumn(name = "STUDENT_ID"))
    private Set<StudentEntity> students = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "COURSE_MODULE",
            joinColumns = @JoinColumn(name = "COURSE_ID"),
            inverseJoinColumns = @JoinColumn(name = "MODULE_ID"))
    private Set<ModuleEntity> modules = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<StudentEntity> getStudents() {
        return students;
    }

    public void setStudents(
            Set<StudentEntity> students) {
        this.students = students;
    }

    public Set<ModuleEntity> getModules() {
        return modules;
    }

    public void setModules(
            Set<ModuleEntity> modules) {
        this.modules = modules;
    }

}