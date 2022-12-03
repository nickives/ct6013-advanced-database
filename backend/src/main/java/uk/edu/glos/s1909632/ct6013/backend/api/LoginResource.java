package uk.edu.glos.s1909632.ct6013.backend.api;

import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uk.edu.glos.s1909632.ct6013.backend.services.UserSessionBean;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {
    @EJB
    UserSessionBean userSessionBean;

    static public final class LoginData {
        @NotEmpty
        public String userId;
    }

    @POST
    public Response login(@Valid LoginData loginData) {
        return userSessionBean.doLogin(loginData.userId)
                .map(Response::ok)
                .orElseThrow(NotFoundException::new)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/logout")
    public Response logout() {
        userSessionBean.doLogout();
        return Response.ok().build();
    }
}