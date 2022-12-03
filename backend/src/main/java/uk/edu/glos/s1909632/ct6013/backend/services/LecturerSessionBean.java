package uk.edu.glos.s1909632.ct6013.backend.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import uk.edu.glos.s1909632.ct6013.backend.api.LecturerResource;
import uk.edu.glos.s1909632.ct6013.backend.api.ModuleResource;
import uk.edu.glos.s1909632.ct6013.backend.persistence.*;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<ModuleResource.ModuleREST> getLecturerModules(String lecturerId) {
        return getEntityFactory().getAllLecturerModules(lecturerId)
                .stream()
                .map(ModuleResource.ModuleREST::new)
                .collect(Collectors.toUnmodifiableList());
    }

    public static final class LecturerStudentMarksREST {
        public static final class StudentMark {
            public final String firstName;
            public final String lastName;
            public final Long mark;

            public StudentMark(Student student, Long mark) {
                this.firstName = student.getFirstName();
                this.lastName = student.getLastName();
                this.mark = mark;
            }
        }

        final public List<StudentMark> studentMarks;
        final public String moduleId;

        public LecturerStudentMarksREST(Module m) {
            this.studentMarks = m.getStudentModules()
                    .stream()
                    .map(sm -> new StudentMark(sm.getStudent(), sm.getMark()))
                    .collect(Collectors.toUnmodifiableList());
            this.moduleId = m.getId()
                    .orElseThrow(IllegalStateException::new);
        }
    }

    public LecturerStudentMarksREST getStudentMarks(
            String lecturerId, String moduleId
    ) {
        Module module = getEntityFactory().getModuleFromLecturer(moduleId, lecturerId)
                .orElseThrow(NotFoundException::new);

        return new LecturerStudentMarksREST(module);
    }

    public static final class SubmitMarksPayload {
        public final String result;
        public SubmitMarksPayload(String result) {
            this.result = result;
        }


    }

    public SubmitMarksPayload submitMarks(String lecturerId, String moduleId,
            LecturerResource.ModuleMarks marks) {
        Module module = getEntityFactory().getModuleFromLecturer(moduleId, lecturerId)
                .orElseThrow(NotFoundException::new);

        Map<String, StudentModule> modules = module.getStudentModules()
                .stream()
                .collect(Collectors.toMap(
                        k -> k.getStudent()
                                .getId()
                                .orElseThrow(IllegalStateException::new),
                        v -> v));
        marks.marks.forEach(m -> Optional.ofNullable(modules.get(m.studentId))
                    .orElseThrow(IllegalStateException::new)
                    .setMark(m.mark));
        return new SubmitMarksPayload("OK");
    }
}
