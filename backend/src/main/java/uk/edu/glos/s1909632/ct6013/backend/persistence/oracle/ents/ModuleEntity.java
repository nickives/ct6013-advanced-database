package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "MODULE")
public class ModuleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "NAME", nullable = false, length = 20)
    private String name;

    @Column(name = "CODE", nullable = false, length = 20)
    private String code;

    @Column(name = "SEMESTER", nullable = false, length = 20)
    private String semester;

    @Column(name = "CAT_POINTS", nullable = false)
    private Long catPoints;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LECTURER_ID", nullable = false)
    private LecturerEntity lecturer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COURSE_ID", nullable = false)
    private CourseEntity course;

    @OneToMany(mappedBy = "module")
    private Set<StudentModuleEntity> studentModules = new LinkedHashSet<>();

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Long getCatPoints() {
        return catPoints;
    }

    public void setCatPoints(Long catPoints) {
        this.catPoints = catPoints;
    }

    public LecturerEntity getLecturer() {
        return lecturer;
    }

    public void setLecturer(LecturerEntity lecturer) {
        this.lecturer = lecturer;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public Set<StudentModuleEntity> getStudentModules() {
        return studentModules;
    }

    public void setStudentModules(Set<StudentModuleEntity> studentModules) {
        this.studentModules = studentModules;
    }

}