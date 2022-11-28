package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.CourseEntity;

import java.util.Optional;
import java.util.Set;
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
                .collect(Collectors.toSet());
    }

    @Override
    public void addModule(Module module) {
        try {
            ModuleOracle moduleOracle = (ModuleOracle) module;
            course.getModules().add(moduleOracle.getEntity());
        } catch (ClassCastException e) {
            throw new IllegalStateException("ModuleOracle class expected", e);
        }
    }

    @Override
    public void save() throws UniqueViolation {
        try {
            if (getId().isPresent()) {
                em.merge(course);
            } else {
                em.persist(course);
            }
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

    CourseEntity getEntity() {
        return course;
    }
}
