package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle;

import jakarta.persistence.*;
import org.hibernate.exception.ConstraintViolationException;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Student;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.CourseEntity;

import java.util.*;
import java.util.stream.Collectors;

public class CourseOracle implements Course {

    private final EntityManager em;
    private final CourseEntity course;

    protected CourseOracle(CourseEntity course, EntityManager em) {
        this.em = em;
        this.course = course;
    }

    protected CourseOracle(EntityManager em) {
        this.em = em;
        this.course = new CourseEntity();
    }

    @Override
    public Optional<String> getId() {
        return Optional.ofNullable(course.getId()).map(Object::toString);
    }

    @Override
    public String getName() {
        return course.getName();
    }

    @Override
    public void setName(String name) {
        course.setName(name);
    }

    @Override
    public Set<Module> getModules() {
        return course.getModules()
                .stream()
                .map(m -> new ModuleOracle(m, em))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public void save() throws UniqueViolation {
        try {
            if (getId().isEmpty()) em.persist(course);
        } catch (PersistenceException e) {
            Throwable cause = e.getCause();
            if (cause.getClass() == ConstraintViolationException.class) {
                ConstraintViolationException cve = (ConstraintViolationException) cause;
                // UNIQUE constraint violation
                if ("23000".equals(cve.getSQLState())) {
                    throw new UniqueViolation(
                            "name",
                            "Course name already exists"
                    );
                }
            }
        }
    }

    @Override
    public void addStudent(Student student) {
        try {
            StudentOracle studentOracle = (StudentOracle) student;
            course.getStudents().add(studentOracle.getEntity());
        } catch (ClassCastException e) {
            throw new IllegalStateException("ModuleOracle class expected", e);
        }
    }

    CourseEntity getEntity() {
        return course;
    }
}
