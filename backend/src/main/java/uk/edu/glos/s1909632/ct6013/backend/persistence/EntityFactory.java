package uk.edu.glos.s1909632.ct6013.backend.persistence;

import java.util.List;
import java.util.Optional;

public interface EntityFactory {

    Lecturer createLecturer();
    Optional<Lecturer> getLecturer(String id);
    List<Lecturer> getAllLecturers();

    Module createModule(Course course);
    Optional<Module> getModuleFromCourse(String moduleId, String courseId);
    Optional<Module> getModuleFromLecturer(String moduleId, String lecturerId);
    List<Module> getModulesFromCourse(List<String> moduleIds, String courseId);
    /***
     * Get modules associated with course
     * @param courseId Course ID
     * @return List of modules
     */
    List<Module> getAllCourseModules(String courseId);

    /***
     * Get modules associated with a student
     * @param studentId Student ID
     * @return List of modules
     */
    List<StudentModule> getAllStudentModules(String studentId);
    List<Module> getAllLecturerModules(String lecturerId);
    Optional<StudentModule> getStudentModule(String studentId, String moduleId);

    Course createCourse();
    Optional<Course> getCourse(String id);
    List<Course> getAllCourses();

    Student createStudent();
    Optional<Student> getStudent(String id);
    List<Student> getAllStudents();
}
