package uk.edu.glos.s1909632.ct6013.backend.api;

import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UnprocessableEntity;
import uk.edu.glos.s1909632.ct6013.backend.services.StudentSessionBean;
import java.util.List;

@Path("/student")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentResource {
    @EJB
    StudentSessionBean studentSessionBean;

    @GET
    public Response getAllStudents() {
        return Response.ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(studentSessionBean.getAllStudents())
                .build();
    }

    public static class StudentInput {
        @NotEmpty
        public String firstName;

        @NotEmpty
        public String lastName;

        @NotEmpty
        public String courseId;

        @NotEmpty
        public String courseYear;
    }

    @POST
    public Response createStudent(
            @Valid StudentInput studentInput,
            @Context UriInfo uriInfo
    ) {
        try {
            StudentSessionBean.StudentREST student = studentSessionBean.createStudent(studentInput);
            return Response.created(
                            UriBuilder.fromPath(uriInfo.getPath()).path("{id}").build(student.id))
                    .type(MediaType.APPLICATION_JSON)
                    .entity(student)
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
    @Path("/{id}")
    public Response getStudent(@PathParam("id") String studentId) {
        return studentSessionBean.getStudent(studentId)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/{id}/results")
    public Response getModules(@PathParam("id") String studentId) {
        return Response.ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(studentSessionBean.getStudentResults(studentId))
                .build();
    }

    public static class AddStudentToModulesInput {
        @NotEmpty
        public List<String> moduleIds;
    }

    @POST
    @Path("/{id}/modules")
    public Response addStudentToModules(
            @PathParam("id") String studentId,
            @Context UriInfo uriInfo,
            @Valid AddStudentToModulesInput input
    ) {
        try {
            StudentSessionBean.RESTCreatedModuleIds createdModuleIds = studentSessionBean.registerStudentOnModules(
                    studentId,
                    input.moduleIds
            );
            return Response.created(UriBuilder.fromPath(uriInfo.getPath()).build())
                    .entity(createdModuleIds)
                    .build();
        } catch (UnprocessableEntity e) {
            RESTError error = new RESTError(
                    "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/422",
                    e.getTitle(),
                    422,
                    e.getDescription(),
                    e.getPropertyName()
            );
            return Response.status(422)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error)
                    .build();
        }
    }

    @GET
    @Path("/{studentId}/modules/{moduleId}")
    public Response getModules(
            @PathParam("studentId") String studentId,
            @PathParam("moduleId") String moduleId
    ) {
        return studentSessionBean.getModule(studentId, moduleId)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}