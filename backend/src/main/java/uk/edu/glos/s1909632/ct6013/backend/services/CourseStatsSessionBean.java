package uk.edu.glos.s1909632.ct6013.backend.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import uk.edu.glos.s1909632.ct6013.backend.CourseStats;
import uk.edu.glos.s1909632.ct6013.backend.persistence.EntityFactory;

import java.util.List;

@Stateless(name = "CourseStatsSessionEJB")
public class CourseStatsSessionBean {

    @EJB
    DbChoiceSessionBean dbChoice;

    public CourseStatsSessionBean() {
    }

    private EntityFactory getEntityFactory() {
        return dbChoice.getEntityFactory();
    }

    public List<CourseStats> getCourseStats() {
        return getEntityFactory().getCourseStats();
    }
}
