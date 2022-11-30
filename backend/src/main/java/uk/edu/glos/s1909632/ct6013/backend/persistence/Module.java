package uk.edu.glos.s1909632.ct6013.backend.persistence;

import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;
import java.util.Optional;
import java.util.Set;

public interface Module {
    void save() throws UniqueViolation;

    Optional<String> getId();

    String getName();

    void setName(String name);

    String getCode();

    void setCode(String code);

    String getSemester();

    void setSemester(String semester);

    Long getCatPoints();

    void setCatPoints(Long catPoints);

    Lecturer getLecturer();

    void setLecturer(Lecturer lecturer);

    Set<Student> getStudents();

}
