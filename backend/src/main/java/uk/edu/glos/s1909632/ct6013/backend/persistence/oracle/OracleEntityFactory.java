package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.NotFoundException;
import uk.edu.glos.s1909632.ct6013.backend.CourseStats;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.*;
import uk.edu.glos.s1909632.ct6013.backend.persistence.*;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        ModuleOracle module = new ModuleOracle(em);
        module.setCourse(course);
        return module;
    }

    @Override
    public Optional<Module> getModuleFromCourse(String moduleId, String courseId) {
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
    public Optional<Module> getModuleFromLecturer(String moduleId, String lecturerId) {
        TypedQuery<ModuleEntity> query = em.createQuery(
                        "SELECT m FROM ModuleEntity m WHERE m.id = :moduleId AND m.lecturer.id = :lecturerId",
                        ModuleEntity.class
                )
                .setParameter("moduleId", moduleId)
                .setParameter("lecturerId", lecturerId);
        try {
            return Optional.of(query.getSingleResult())
                    .map(m -> new ModuleOracle(m, em));
        } catch (NoResultException e) {
            throw new NotFoundException(e);
        }
    }

    @Override
    public List<Module> getModulesFromCourse(List<String> moduleIds, String courseId) {
        List<Long> longModuleIds = moduleIds.stream()
                .map(Long::parseLong)
                .collect(Collectors.toUnmodifiableList());
        TypedQuery<ModuleEntity> query = em.createQuery(
                        "SELECT m FROM ModuleEntity m WHERE m.id IN :moduleIds AND m.course.id = :courseId",
                        ModuleEntity.class
                )
                .setParameter("moduleIds", longModuleIds)
                .setParameter("courseId", courseId);
        return query.getResultList()
                .stream()
                .map(m -> new ModuleOracle(m, em))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Module> getAllCourseModules(String courseId) {
        TypedQuery<ModuleEntity> query = em.createQuery(
                        "SELECT m FROM ModuleEntity m WHERE m.course.id = :courseId",
                        ModuleEntity.class)
                .setParameter("courseId", courseId);
        return query.getResultList()
                .stream()
                .map(m -> new ModuleOracle(m, em))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<StudentModule> getAllStudentModules(String studentId) {
        return em.createQuery(
                "SELECT m FROM StudentModuleEntity m WHERE m.id.studentId = :studentId",
                StudentModuleEntity.class)
                .setParameter("studentId", studentId)
                .getResultList()
                .stream()
                .map(m -> new StudentModuleOracle(m, em))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Module> getAllLecturerModules(String lecturerId) {
        return em.createQuery(
                        "SELECT m FROM ModuleEntity m WHERE m.lecturer.id = :lecturerId",
                        ModuleEntity.class)
                .setParameter("lecturerId", lecturerId)
                .getResultList()
                .stream()
                .map(m -> new ModuleOracle(m, em))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<StudentModule> getStudentModule(String studentId, String moduleId) {
        TypedQuery<StudentModuleEntity> query = em.createQuery(
                "SELECT m FROM StudentModuleEntity m " +
                        "WHERE m.id.moduleId = :moduleId AND m.id.studentId = :studentId",
                StudentModuleEntity.class)
                .setParameter("studentId", studentId)
                .setParameter("moduleId", moduleId);

        try {
            return Optional.of(query.getSingleResult())
                    .map(m -> new StudentModuleOracle(m, em));
        } catch (NoResultException e) {
            return Optional.empty();
        }
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
        return new StudentOracle(em);
    }

    @Override
    public Optional<Student> getStudent(String id) {
        return Optional.ofNullable(em.find(StudentEntity.class, id))
                .map(s -> new StudentOracle(s, em));
    }

    @Override
    public List<Student> getAllStudents() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<StudentEntity> criteria = builder.createQuery(StudentEntity.class);
        Root<StudentEntity> root = criteria.from(StudentEntity.class);
        criteria.select(root);
        return em.createQuery(criteria)
                .getResultList()
                .stream()
                .map(student -> new StudentOracle(student, em))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<CourseStats> getCourseStats() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ViewCourseGradeSplit> courseGradeCriteria = builder
                .createQuery(ViewCourseGradeSplit.class);

        Root<ViewCourseGradeSplit> courseGradeRoot = courseGradeCriteria.
                from(ViewCourseGradeSplit.class);

        courseGradeCriteria.select(courseGradeRoot);
        List<ViewCourseGradeSplit> courseGradeSplits = em.createQuery(courseGradeCriteria)
                .getResultList();

        CriteriaQuery<ViewModuleAggregateResult> moduleAggregateCriteria = builder
                .createQuery(ViewModuleAggregateResult.class);

        Root<ViewModuleAggregateResult> moduleAggregateRoot = moduleAggregateCriteria
                .from(ViewModuleAggregateResult.class);

        moduleAggregateCriteria.select(moduleAggregateRoot);

        // Key is CourseId + CourseYear
        Map<String, List<CourseStats.ModuleStats>> moduleAggregateResults = em.createQuery(moduleAggregateCriteria)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(
                        // Key mapper
                        k -> k.getCourseId().toString() + k.getCourseYear(),
                        // Value mapper
                        m -> List.of(new CourseStats.ModuleStats(
                                m.getModuleId().toString(), m.getModuleName(),
                                m.getAverageMark(), m.getNumberOfStudents())),
                        // Merge function
                        (previous, current) -> {
                            List<CourseStats.ModuleStats> newList = new ArrayList<>(previous);
                            newList.addAll(current);
                            return newList;
                        }));


        List<CourseStats> courseStats = new ArrayList<>();
        courseGradeSplits.forEach(cg -> {
            CourseStats stats = new CourseStats(cg.getCourseId().toString(), cg.getCourseName(),
                                                cg.getCourseYear(), moduleAggregateResults.get(
                                                        cg.getCourseId().toString() + cg.getCourseYear()),
                                                cg.getAverageMark(), cg.getFirst(),
                                                cg.getTwoOne(), cg.getTwoTwo(), cg.getThird(),
                                                cg.getFail());
            courseStats.add(stats);
        });

        return courseStats;
    }
}
