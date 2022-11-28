package uk.edu.glos.s1909632.ct6013.backend.api;

import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UniqueViolation;
import uk.edu.glos.s1909632.ct6013.backend.exceptions.UnprocessableEntity;
import uk.edu.glos.s1909632.ct6013.backend.persistence.Module;
import uk.edu.glos.s1909632.ct6013.backend.services.ModuleSessionBean;

import java.util.List;
import java.util.stream.Collectors;

@Path("/course/{courseId}/modules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModuleResource {
    public static final class ModuleREST {
        public final String id;
        public final String name;
        public final String code;
        public final String semester;
        public final Number catPoints;
        public final String lecturer;

        public ModuleREST(Module module) {
            this.id = module.getId()
                    .orElseThrow(() -> new IllegalStateException("Missing Module ID"));
            this.name = module.getName();
            this.code = module.getCode();
            this.semester = module.getSemester();
            this.catPoints = module.getCatPoints();
            this.lecturer = "/lecturer/" +
                    module.getLecturer()
                            .getId()
                            .orElseThrow(() -> new IllegalStateException("Missing Lecturer ID"));
        }
    }

    @EJB
    ModuleSessionBean moduleSessionBean;

    @GET
    public Response getAllModules(@PathParam("courseId") String courseId) {
        List<ModuleREST> modules = moduleSessionBean.getAllModules(courseId)
                .stream().map(ModuleREST::new)
                .collect(Collectors.toList());

        return Response.ok()
                .type(MediaType.APPLICATION_JSON)
                .entity(modules)
                .build();
    }

    public static final class ModuleInput {
        @NotEmpty
        public String name;
        @NotEmpty
        public String code;
        @NotEmpty
        public String semester;
        @NotNull
        @Min(value = 5, message = "CAT Points must be at least 5")
        @Max(value = 30, message = "CAT Points cannot be greater than 30")
        public Long catPoints;
        @NotEmpty
        public String lecturerId;
    }

    @POST
    public Response createModule(
            @Valid ModuleInput moduleInput,
            @PathParam("courseId") String courseId,
            @Context UriInfo uriInfo
    ) {
        try {
            ModuleREST module = new ModuleREST(
                    moduleSessionBean.createModule(moduleInput, courseId)
            );
            return Response.created(UriBuilder.fromPath(uriInfo.getPath())
                                            .path("{id}").build(module.id))
                    .type(MediaType.APPLICATION_JSON)
                    .entity(module)
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
        } catch (UniqueViolation e) {
            RESTError error = new RESTError(
                    "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/409",
                    e.getMessage(),
                    Response.Status.CONFLICT.getStatusCode(),
                    "Field value must be unique",
                    e.getPropertyName()
            );
            return Response.status(Response.Status.CONFLICT)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(error)
                    .build();
        }
    }

    @GET
    @Path("/{moduleId}")
    public Response getModule(
            @PathParam("courseId") String courseId,
            @PathParam("moduleId") String moduleId
    ) {
        return moduleSessionBean.getModule(courseId, moduleId)
                .map(m -> Response.ok(new ModuleREST(m)))
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}