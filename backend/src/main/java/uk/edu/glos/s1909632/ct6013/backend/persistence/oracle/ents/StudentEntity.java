package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import uk.edu.glos.s1909632.ct6013.backend.Grade;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "STUDENT")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "FIRST_NAME", nullable = false, length = 20)
    private String firstName;

    @Nationalized
    @Column(name = "LAST_NAME", nullable = false, length = 20)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COURSE_ID", nullable = false)
    private CourseEntity course;

    @OneToMany(mappedBy = "student")
    private Set<StudentModuleEntity> studentModules = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    private Grade grade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity courses) {
        this.course = courses;
    }

    public Set<StudentModuleEntity> getStudentModules() {
        return studentModules;
    }

    public void setStudentModules(Set<StudentModuleEntity> studentModules) {
        this.studentModules = studentModules;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentEntity that = (StudentEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}