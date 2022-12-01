package uk.edu.glos.s1909632.ct6013.backend.api;
import jakarta.ws.rs.*;

@Path("/hello-world")
public class LoginResource {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
}