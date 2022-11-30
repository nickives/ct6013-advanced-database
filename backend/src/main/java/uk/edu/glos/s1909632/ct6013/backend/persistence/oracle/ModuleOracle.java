package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Lecturer;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Student;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.ModuleEntity;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class ModuleOracle implements Module {
    private final ModuleEntity module;
    private final EntityManager em;

    /**
     * Construct module retrieved from database
     * @param module ModuleEntity from database
     * @param em EntityManager
     */
    ModuleOracle(ModuleEntity module, EntityManager em) {
        this.module = module;
        this.em = em;

    }

    /**
     * Construct new module
     * @param em EntityManager
     */
    ModuleOracle(EntityManager em) {
        this.module = new ModuleEntity();
        this.em = em;
    }

    @Override
    @Transactional
    public void save() throws UniqueViolation {
        try {
            if (getId().isPresent()) {
                em.merge(module);
            } else {
                em.persist(module);
            }
        } catch (PersistenceException e) {
            Throwable cause = e.getCause();
            if (cause.getClass() == ConstraintViolationException.class) {
                ConstraintViolationException cve = (ConstraintViolationException) cause;
                // UNIQUE constraint violation
                if ("23000".equals(cve.getSQLState())) {
                    throw new UniqueViolation(
                            "code",
                            "Module code already exists"
                    );
                }
            }
        }
    }

    @Override
    public Optional<String> getId() {
        return Optional.ofNullable(module.getId()).map(Object::toString);
    }

    @Override
    public String getName() {
        return module.getName();
    }

    @Override
    public void setName(String name) {
        module.setName(name);
    }

    @Override
    public String getCode() {
        return module.getCode();
    }

    @Override
    public void setCode(String code) {
        module.setCode(code);
    }

    @Override
    public String getSemester() {
        return module.getSemester();
    }

    @Override
    public void setSemester(String semester) {
        module.setSemester(semester);
    }

    @Override
    public Long getCatPoints() {
        return module.getCatPoints();
    }

    @Override
    public void setCatPoints(Long catPoints) {
        module.setCatPoints(catPoints);
    }

    @Override
    public Lecturer getLecturer() {
        return new LecturerOracle(module.getLecturer(), em);
    }

    @Override
    @Transactional
    public void setLecturer(Lecturer lecturer) {
        try {
            LecturerOracle lecturerOracle = (LecturerOracle) lecturer;
            module.setLecturer(lecturerOracle.getEntity());
        } catch (ClassCastException e) {
            throw new IllegalStateException("LecturerOracle class expected", e);
        }
    }

    @Override
    public Set<Student> getStudents() {
        return module.getStudentModules()
                .stream()
                .map(sme -> new StudentOracle(sme.getStudent(), em))
                .collect(Collectors.toSet());
    }

    public void setCourse(Course course) {
        try {
            CourseOracle courseOracle = (CourseOracle) course;
            module.setCourse(courseOracle.getEntity());
        } catch (ClassCastException e) {
            throw new IllegalStateException("CourseOracle class expected", e);
        }
    }

    ModuleEntity getEntity() {
        return module;
    }
}
