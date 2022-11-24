package uk.edu.glos.s1909632.ct6013.backend.api;

import jakarta.ejb.EJB;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Lecturer;
import uk.edu.glos.s1909632.ct6013.backend.services.LecturerSessionBean;
import java.util.List;
import java.util.stream.Collectors;

@Path("/lecturer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LecturerResource {
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
    LecturerSessionBean lecturerSessionBean;

    @GET
    @Path("/{id}")
    public Response getLecturer(@PathParam("id") String id) {
        return lecturerSessionBean.getLecturer(id)
                .map(l -> Response.ok(new LecturerREST(l)))
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    public static class LecturerInput {
        @NotEmpty
        public String name;
    }

    @POST
    public Response createLecturer(LecturerInput lecturerInput, @Context UriInfo uriInfo) {
        try {
            LecturerREST lecturer = new LecturerREST(
                    lecturerSessionBean.createLecturer(lecturerInput.name)
            );

            return Response.created(UriBuilder.fromPath(uriInfo.getPath()).path("{id}").build(lecturer.id))
                    .type(MediaType.APPLICATION_JSON)
                    .entity(lecturer)
                    .build();
        } catch (Throwable e) {
            return Response.serverError()
                    .type(MediaType.APPLICATION_JSON)
                    .status(500)
                    .entity(e)
                    .build();
        }
    }

    @GET
    public Response getAllLecturers() {
        List<LecturerREST> lecturers = lecturerSessionBean.getAllLecturers()
                .stream()
                .map(LecturerREST::new)
                .collect(Collectors.toList());

        return Response.ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(lecturers)
                .build();
    }
}