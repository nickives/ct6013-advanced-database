package uk.edu.glos.s1909632.ct6013.backend.persistence;

import java.util.List;
import java.util.Optional;

public interface EntityFactory {

    Lecturer createLecturer();
    Optional<Lecturer> getLecturer(String id);
    List<Lecturer> getAllLecturers();

    Module createModule(Course course);
    Optional<Module> getModule(String moduleId, String courseId);

    List<Module> getModules(String courseId);

    Course createCourse();
    Optional<Course> getCourse(String id);
    List<Course> getAllCourses();

    Student createStudent();
    Optional<Student> getStudent(String id);
    List<Student> getAllStudents();
}
