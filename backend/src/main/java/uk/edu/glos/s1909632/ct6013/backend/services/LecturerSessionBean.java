package uk.edu.glos.s1909632.ct6013.backend.services;

import jakarta.ejb.EJB;
import jakarta.transaction.Transactional;
import uk.edu.glos.s1909632.ct6013.backend.persistence.EntityFactory;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Lecturer;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@jakarta.ejb.Stateless(name = "LecturerSessionEJB")
public class LecturerSessionBean {

    public static class LecturerREST {
        public final String id;
        public final String name;
        public final String modules;

        LecturerREST(Lecturer lecturer) {
            id = lecturer.getId()
                    .orElseThrow(() -> new IllegalStateException("Missing Lecturer ID"));
            name = lecturer.getName();
            modules = "/lecturer/" +
                    id +
                    "/modules";
        }
    }

    @EJB
    DbChoiceSessionBean dbChoice;

    public LecturerSessionBean() {
    }

    private EntityFactory getEntityFactory() {
        return dbChoice.getEntityFactory();
    }

    public Optional<LecturerREST> getLecturer(String id) {
        return getEntityFactory().getLecturer(id)
                .map(LecturerREST::new);
    }

    @Transactional
    public LecturerREST createLecturer(String name) {
        Lecturer lecturer = getEntityFactory().createLecturer();
        lecturer.setName(name);
        lecturer.save();
        return new LecturerREST(lecturer);
    }

    public List<LecturerREST> getAllLecturers() {
        return Optional.of(getEntityFactory().getAllLecturers())
                .map(l -> l.stream()
                        .map(LecturerREST::new)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }
}
