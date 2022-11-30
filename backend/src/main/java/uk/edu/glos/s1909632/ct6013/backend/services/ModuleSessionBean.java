package uk.edu.glos.s1909632.ct6013.backend.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import uk.edu.glos.s1909632.ct6013.backend.api.ModuleResource;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UnprocessableEntity;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.persistence.EntityFactory;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Lecturer;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;

import java.util.List;
import java.util.Optional;

@Stateless(name = "ModuleSessionEJB")
public class ModuleSessionBean {
    @EJB
    DbChoiceSessionBean dbChoice;

    public ModuleSessionBean() {
    }

    private EntityFactory getEntityFactory() {
        return dbChoice.getEntityFactory();
    }

    public Optional<Module> getModule(String courseId, String moduleId ) {
        return getEntityFactory().getModuleFromCourse(moduleId, courseId);
    }

    @Transactional
    public Module createModule(ModuleResource.ModuleInput moduleInput, String courseId)
            throws UnprocessableEntity, UniqueViolation {
        Course course = getEntityFactory().getCourse(courseId)
                .orElseThrow(NotFoundException::new);
        Lecturer lecturer;
        try {
            lecturer = getEntityFactory().getLecturer(moduleInput.lecturerId)
                    .orElseThrow(() -> new UnprocessableEntity(
                            "lecturerId",
                            "Invalid lecturer ID"
                    ));
        } catch (NotFoundException e) {
            throw  new UnprocessableEntity(
                    "lecturerId",
                    "Invalid lecturer ID"
            );
        }

        Module module = getEntityFactory().createModule(course);
        module.setName(moduleInput.name);
        module.setCode(moduleInput.code);
        module.setSemester(moduleInput.semester);
        module.setCatPoints(moduleInput.catPoints);
        module.setLecturer(lecturer);
        module.save();
        return module;
    }

    public List<Module> getAllModules(String courseId) {
        return getEntityFactory().getAllCourseModules(courseId);
    }
}
