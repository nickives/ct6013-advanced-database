package uk.edu.glos.s1909632.ct6013.backend.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import uk.edu.glos.s1909632.ct6013.backend.Grade;
import uk.edu.glos.s1909632.ct6013.backend.api.ModuleResource;
import uk.edu.glos.s1909632.ct6013.backend.api.StudentResource;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UnprocessableEntity;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.persistence.EntityFactory;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Student;
import uk.edu.glos.s1909632.ct6013.backend.persistence.StudentModule;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless(name = "StudentSessionEJB")
public class StudentSessionBean {
    public static class StudentREST {
        public final String id;
        public final String firstName;
        public final String lastName;
        public final String modules;
        public final String course;
        public final String courseYear;

        public StudentREST(Student student) {
            this.id = student.getId()
                    .orElseThrow(() -> new IllegalStateException("Missing Student ID"));
            this.firstName = student.getFirstName();
            this.lastName = student.getLastName();
            this.modules = "/api/student/" + id + "/modules";
            this.course = "/api/course/" + student.getCourse()
                    .getId()
                    .orElseThrow(() -> new IllegalStateException("Missing Course ID"));
            this.courseYear = student.getCourseYear();
        }
    }

    public static final class StudentModuleREST {
        public final ModuleResource.ModuleREST module;
        public final Long mark;

        public StudentModuleREST(StudentModule studentModule) {
            this.mark = studentModule.getMark();
            this.module = new ModuleResource.ModuleREST(studentModule.getModule());
        }
    }

    public static final class CourseResultREST {
        public final List<StudentModuleREST> moduleResults;
        public final Long averageMark;
        public final String grade;

        public CourseResultREST(List<StudentModuleREST> moduleResults, Long averageMark,
                                Grade grade) {
            this.moduleResults = moduleResults;
            this.averageMark = averageMark;
            this.grade = grade.toString();
        }
    }

    @EJB
    DbChoiceSessionBean dbChoice;

    public StudentSessionBean() {}

    private EntityFactory getEntityFactory() {
        return dbChoice.getEntityFactory();
    }

    public Optional<StudentREST> getStudent(String id) {
        return getEntityFactory().getStudent(id).map(StudentREST::new);
    }

    @Transactional
    public StudentREST createStudent(StudentResource.StudentInput studentInput) throws UnprocessableEntity {
        Course course = getEntityFactory().getCourse(studentInput.courseId)
                .orElseThrow(() -> new UnprocessableEntity(
                        "courseId", "Course not found")
                );
        Student student = getEntityFactory().createStudent();
        student.setFirstName(studentInput.firstName);
        student.setLastName(studentInput.lastName);
        student.setCourseYear(studentInput.courseYear);
        student.setCourse(course);
        student.save();
        return new StudentREST(student);
    }

    public List<StudentREST> getAllStudents() {
        return getEntityFactory().getAllStudents()
                .stream()
                .map(StudentREST::new)
                .collect(Collectors.toUnmodifiableList());
    }

    public CourseResultREST getStudentResults(String studentId) {
        Student student = getEntityFactory().getStudent(studentId)
                .orElseThrow(NotFoundException::new);

        List<StudentModuleREST> studentModules = student.getModules()
                .stream()
                .map(StudentModuleREST::new)
                .collect(Collectors.toUnmodifiableList());

        Double averageMark = studentModules.stream()
                .filter(m -> m.mark != null)
                .mapToLong(m -> m.mark)
                .average()
                .orElse(0);

        Grade grade = student.getGrade();
        return new CourseResultREST(studentModules, averageMark.longValue(), grade);
    }

    public Optional<StudentModuleREST> getModule(String studentId, String moduleId) {
        return getEntityFactory().getStudentModule(studentId, moduleId)
                .map(StudentModuleREST::new);
    }

    public static class RESTCreatedModuleIds {
        public final List<String> moduleIds;

        public RESTCreatedModuleIds(List<String> moduleIds) {
            this.moduleIds = moduleIds;
        }

    }

    @Transactional
    public RESTCreatedModuleIds registerStudentOnModules(String studentId, List<String> moduleIds) throws UnprocessableEntity {
        Student student = getEntityFactory().getStudent(studentId)
                .orElseThrow(NotFoundException::new);
        Long currentStudentCatPoints = getEntityFactory().getAllStudentModules(studentId)
                .stream()
                .map(m -> m.getModule().getCatPoints())
                .reduce(Long::sum)
                .orElse(0L);

        if (currentStudentCatPoints >= 120) {
            throw new UnprocessableEntity(
                    "moduleIds",
                    "Student already registered on modules");
        }

        String courseId = student.getCourse()
                .getId()
                .orElseThrow(() -> new IllegalStateException("Student missing course"));
        List<Module> modules = getEntityFactory().getModulesFromCourse(moduleIds, courseId);

        Long catPointsTotal = modules.stream()
                .map(Module::getCatPoints)
                .reduce(Long::sum)
                .orElse(0L);

        if (catPointsTotal != 120L) {
            throw new UnprocessableEntity(
                    "moduleIds",
                    "Require exactly 120 CAT Points. " + catPointsTotal + " selected");
        }

        modules.forEach(student::addToModule);
        student.save();
        return new RESTCreatedModuleIds(moduleIds);
    }
}
