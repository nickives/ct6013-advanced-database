package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "STUDENT", indexes = {
        @Index(name = "STUDENT_STUDENT_NUMBER_uindex", columnList = "STUDENT_NUMBER", unique = true)
})
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

    @Nationalized
    @Column(name = "STUDENT_NUMBER", nullable = false, length = 20)
    private String studentNumber;

    @ManyToMany
    @JoinTable(name = "STUDENT_COURSE",
            joinColumns = @JoinColumn(name = "STUDENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "COURSE_ID"))
    private Set<CourseEntity> courses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentModuleEntity> studentModules = new LinkedHashSet<>();

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

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Set<CourseEntity> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseEntity> courses) {
        this.courses = courses;
    }

    public Set<StudentModuleEntity> getStudentModules() {
        return studentModules;
    }

    public void setStudentModules(Set<StudentModuleEntity> studentModules) {
        this.studentModules = studentModules;
    }

}