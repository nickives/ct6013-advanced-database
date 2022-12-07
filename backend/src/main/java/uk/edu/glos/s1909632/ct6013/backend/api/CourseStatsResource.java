package uk.edu.glos.s1909632.ct6013.backend.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uk.edu.glos.s1909632.ct6013.backend.services.CourseStatsSessionBean;

@Path("/course-stats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CourseStatsResource {
    @EJB
    CourseStatsSessionBean courseStatsSessionBean;

    @GET
    public Response getCourseStats() {
        return Response.ok(courseStatsSessionBean.getCourseStats())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
