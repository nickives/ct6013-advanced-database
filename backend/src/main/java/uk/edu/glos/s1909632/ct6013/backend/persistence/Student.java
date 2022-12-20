package uk.edu.glos.s1909632.ct6013.backend.persistence;

import uk.edu.glos.s1909632.ct6013.backend.Grade;

import java.util.Optional;
import java.util.Set;

public interface Student {
    void save();
    Optional<String> getId();
    String getFirstName();
    void setFirstName(String firstName);
    String getLastName();
    void setLastName(String lastName);
    Set<StudentModule> getModules();
    Course getCourse();
    void setCourse(Course course);
    void addToModule(Module module);
    Grade getGrade();
    void setGrade(Grade grade);
    String getCourseYear();
    void setCourseYear(String courseYear);
}
