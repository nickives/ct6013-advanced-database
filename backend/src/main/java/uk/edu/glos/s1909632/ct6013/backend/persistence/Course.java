package uk.edu.glos.s1909632.ct6013.backend.persistence;

import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;

import java.util.Optional;
import java.util.Set;

public interface Course {
    Optional<String> getId();
    String getName();
    void setName(String name);
    Set<Module> getModules();
    void addModule(Module module);
    void save() throws UniqueViolation;
}
