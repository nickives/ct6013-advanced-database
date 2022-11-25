package uk.edu.glos.s1909632.ct6013.backend.api;

import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Course;
import uk.edu.glos.s1909632.ct6013.backend.persistence.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.services.CourseSessionBean;

import java.util.List;
import java.util.stream.Collectors;

@Path("/course")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class CourseResource {
    public static final class CourseREST {
        public final String id;
        public final String name;
        public final String modules;

        public CourseREST(Course course) {
            this.id = course.getId()
                    .orElseThrow(() -> new IllegalStateException("Missing Course ID"));
            this.name = course.getName();
            this.modules = "/course/" +
                    id +
                    "/modules";
        }
    }

    @EJB
    CourseSessionBean courseSessionBean;

    @GET
    public Response getAllCourses() {
        List<CourseREST> courses = courseSessionBean.getALlCourses().stream().map(CourseREST::new).collect(
                Collectors.toList());

        return Response.ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(courses)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getCourse(@PathParam("id") String id) {
        return courseSessionBean.getCourse(id)
                .map(c -> Response.ok(new CourseREST(c)))
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    public static class CourseInput {
        @NotEmpty
        public String name;
    }

    @POST
    public Response createCourse(@Valid CourseInput courseInput, @Context UriInfo uriInfo) {
        try {
            CourseREST course = new CourseREST(
                    courseSessionBean.createCourse(courseInput.name)
            );

            return Response.created(UriBuilder.fromPath(uriInfo.getPath()).path("{id}").build(course.id))
                    .type(MediaType.APPLICATION_JSON)
                    .entity(course)
                    .build();
        } catch (UniqueViolation e) {
            RESTError error = new RESTError(
                    e.getMessage(),
                    Response.Status.CONFLICT.getStatusCode(),
                    e.getPropertyName()
            );
            return Response.status(Response.Status.CONFLICT)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error)
                    .build();
        } catch (Throwable e) {
            return Response.serverError()
                    .type(MediaType.APPLICATION_JSON)
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e)
                    .build();
        }
    }
}