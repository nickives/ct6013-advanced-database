package uk.edu.glos.s1909632.ct6013.backend.persistence;

import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;

import java.util.Optional;

public interface Course {
    Optional<String> getId();
    String getName();
    void setName(String name);
    void save() throws UniqueViolation;
}
