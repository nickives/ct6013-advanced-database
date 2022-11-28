package uk.edu.glos.s1909632.ct6013.backend.persistence;

import java.util.Optional;
import java.util.Set;

public interface Lecturer {
    void save();
    Optional<String> getId();
    void setName(String name);
    String getName();
    Set<Module> getModules();
}
