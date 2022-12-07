package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Objects;
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

    @OneToMany(mappedBy = "course")
    private Set<StudentEntity> students = new LinkedHashSet<>();

    @OneToMany(mappedBy = "course")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseEntity that = (CourseEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}