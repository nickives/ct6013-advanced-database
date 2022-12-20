package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle;

import jakarta.persistence.EntityManager;
import uk.edu.glos.s1909632.ct6013.backend.Grade;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Student;
import uk.edu.glos.s1909632.ct6013.backend.persistence.StudentModule;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.ModuleEntity;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.StudentEntity;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.StudentModuleEntity;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.StudentModuleEntityId;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentOracle implements Student {
    private final StudentEntity student;
    private final EntityManager em;

    public StudentOracle(StudentEntity student, EntityManager em) {
        this.student = student;
        this.em = em;
    }

    public StudentOracle(EntityManager em) {
        this.student = new StudentEntity();
        this.em = em;
    }

    @Override
    public void save() {
        if (getId().isPresent()) {
            em.merge(student);
        } else {
            em.persist(student);
        }
    }

    @Override
    public Optional<String> getId() {
        return Optional.ofNullable(student.getId()).map(Object::toString);
    }

    @Override
    public String getFirstName() {
        return student.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        student.setFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return student.getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        student.setLastName(lastName);
    }

    @Override
    public Set<StudentModule> getModules() {
        return student.getStudentModules()
                .stream()
                .map(m -> new StudentModuleOracle(m, em))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Course getCourse() {
        return new CourseOracle(student.getCourse(), em);
    }

    @Override
    public void setCourse(Course course) {
        try {
            CourseOracle courseOracle = (CourseOracle) course;
            student.setCourse(courseOracle.getEntity());
        } catch (ClassCastException e) {
            throw new IllegalStateException("CourseOracle expected", e);
        }
    }

    @Override
    public void addToModule(Module module) {
        ModuleOracle moduleOracle;
        try {
            moduleOracle = (ModuleOracle) module;
        } catch (ClassCastException e) {
            throw new IllegalStateException("StudentOracle expected", e);
        }
        StudentModuleEntity sme = new StudentModuleEntity();
        StudentModuleEntityId smid = new StudentModuleEntityId();
        ModuleEntity moduleEntity = moduleOracle.getEntity();
        smid.setModuleId(moduleEntity.getId());
        smid.setStudentId(student.getId());
        sme.setId(smid);
        sme.setStudent(student);
        sme.setModule(moduleEntity);
        em.persist(sme);
    }

    @Override
    public Grade getGrade() {
        return student.getGrade();
    }

    @Override
    public void setGrade(Grade grade) {
        student.setGrade(grade);
    }

    @Override
    public String getCourseYear() {
        return student.getCourseYear();
    }

    @Override
    public void setCourseYear(String courseYear) {
        student.setCourseYear(courseYear);
    }

    StudentEntity getEntity() {
        return student;
    }
}
