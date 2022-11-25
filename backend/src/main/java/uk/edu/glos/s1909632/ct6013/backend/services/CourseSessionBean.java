package uk.edu.glos.s1909632.ct6013.backend.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.persistence.EntityFactory;
import uk.edu.glos.s1909632.ct6013.backend.persistence.exceptions.UniqueViolation;

import java.util.List;
import java.util.Optional;

@Stateless(name = "CourseSessionEJB")
public class CourseSessionBean {

    @EJB
    DbChoiceSessionBean dbChoice;

    public CourseSessionBean() {
    }

    private EntityFactory getEntityFactory() {
        return dbChoice.getEntityFactory();
    }

    public Optional<Course> getCourse(String id) {
        return getEntityFactory().getCourse(id);
    }

    @Transactional
    public Course createCourse(String name) throws UniqueViolation {
        Course course = getEntityFactory().createCourse();
        course.setName(name);
        course.save();
        return course;
    }

    public List<Course> getALlCourses() {
        return getEntityFactory().getAllCourses();
    }
}
