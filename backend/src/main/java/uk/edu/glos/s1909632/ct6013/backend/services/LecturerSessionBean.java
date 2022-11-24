package uk.edu.glos.s1909632.ct6013.backend.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import uk.edu.glos.s1909632.ct6013.backend.persistence.EntityFactory;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Lecturer;
import java.util.List;
import java.util.Optional;

@Stateless(name = "LecturerSessionEJB")
public class LecturerSessionBean {

    @EJB
    DbChoiceSessionBean dbChoice;

    public LecturerSessionBean() {
    }

    private EntityFactory getEntityFactory() {
        return dbChoice.getEntityFactory();
    }

    public Optional<Lecturer> getLecturer(String id) {
        return getEntityFactory().getLecturer(id);
    }

    @Transactional
    public Lecturer createLecturer(String name) {
        Lecturer lecturer = getEntityFactory().createLecturer();
        lecturer.setName(name);
        lecturer.save();
        return lecturer;
    }

    public List<Lecturer> getAllLecturers() {
        return getEntityFactory().getAllLecturers();
    }
}
