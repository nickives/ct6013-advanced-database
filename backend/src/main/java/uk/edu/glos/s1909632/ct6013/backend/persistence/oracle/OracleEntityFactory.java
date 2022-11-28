package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.NotFoundException;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.CourseEntity;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.LecturerEntity;
import uk.edu.glos.s1909632.ct6013.backend.persistence.*;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.ModuleEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OracleEntityFactory implements EntityFactory {

    private final EntityManager em;

    public OracleEntityFactory(EntityManager em) {
        this.em = em;
    }

    @Override
    public Lecturer createLecturer() {
        return new LecturerOracle(em);
    }

    @Override
    public Optional<Lecturer> getLecturer(String id) {
        return Optional.ofNullable(em.find(LecturerEntity.class, id))
                .map(lecturer -> new LecturerOracle(lecturer, em));
    }

    @Override
    public List<Lecturer> getAllLecturers() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<LecturerEntity> criteria = builder.createQuery(LecturerEntity.class);
        Root<LecturerEntity> root = criteria.from(LecturerEntity.class);
        criteria.select(root);
        return em.createQuery(criteria)
                .getResultList()
                .stream()
                .map(lecturer -> new LecturerOracle(lecturer, em))
                .collect(Collectors.toList());
    }

    @Override
    public Module createModule(Course course) {
        Module module = new ModuleOracle(em);
        module.setCourse(course);
        return module;
    }

    @Override
    public Optional<Module> getModule(String moduleId, String courseId) {
        TypedQuery<ModuleEntity> query = em.createQuery(
                "SELECT m FROM ModuleEntity m WHERE m.id = :moduleId AND m.course.id = :courseId",
                ModuleEntity.class
        )
                .setParameter("moduleId", moduleId)
                .setParameter("courseId", courseId);
        try {
            return Optional.of(query.getSingleResult())
                    .map(m -> new ModuleOracle(m, em));
        } catch (NoResultException e) {
            throw new NotFoundException(e);
        }
    }

    @Override
    public List<Module> getModules(String courseId) {
        TypedQuery<ModuleEntity> query = em.createQuery(
                        "SELECT m FROM ModuleEntity m WHERE m.course.id = :courseId",
                        ModuleEntity.class)
                .setParameter("courseId", courseId);
        return query.getResultList()
                .stream()
                .map(m -> new ModuleOracle(m, em))
                .collect(Collectors.toList());
    }

    @Override
    public Course createCourse() {
        return new CourseOracle(em);
    }

    @Override
    public Optional<Course> getCourse(String id) {
        return Optional.ofNullable(em.find(CourseEntity.class, id))
                .map(course -> new CourseOracle(course, em));
    }

    @Override
    public List<Course> getAllCourses() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CourseEntity> criteria = builder.createQuery(CourseEntity.class);
        Root<CourseEntity> root = criteria.from(CourseEntity.class);
        criteria.select(root);
        return em.createQuery(criteria)
                .getResultList()
                .stream()
                .map(course -> new CourseOracle(course, em))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Student createStudent() {
        return null;
    }

    @Override
    public Optional<Student> getStudent(String id) {
        return Optional.empty();
    }

    @Override
    public List<Student> getAllStudents() {
        return null;
    }
}
