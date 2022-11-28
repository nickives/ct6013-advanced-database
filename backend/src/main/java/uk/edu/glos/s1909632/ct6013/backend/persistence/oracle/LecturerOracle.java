package uk.edu.glos.s1909632.ct6013.backend.persistence.oracle;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.ents.LecturerEntity;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Lecturer;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;

import java.util.Optional;
import java.util.Set;

public class LecturerOracle implements Lecturer {

    private final EntityManager em;
    private final LecturerEntity lecturer;

    protected LecturerOracle(LecturerEntity lecturer, EntityManager em) {
        this.lecturer = lecturer;
        this.em = em;
    }

    protected LecturerOracle(EntityManager em) {
        this.lecturer = new LecturerEntity();
        this.em = em;
    }

    @Override
    @Transactional
    public void save() {
        if (getId().isPresent()) {
            em.merge(lecturer);
        } else {
            em.persist(lecturer);
        }
    }

    @Override
    public Optional<String> getId() {
        return Optional.ofNullable(lecturer.getId()).map(Object::toString);
    }

    @Override
    @Transactional
    public void setName(String name) {
        lecturer.setName(name);
    }

    @Override
    public String getName() {
        return lecturer.getName();
    }

    @Override
    public Set<Module> getModules() {
        return null;
    }

    protected LecturerEntity getEntity() {
        return lecturer;
    }
}
