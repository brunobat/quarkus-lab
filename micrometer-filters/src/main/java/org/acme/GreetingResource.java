package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@QueryParam("op") String op) {
        if(op != null && op.equals("error")) {
            throw new RuntimeException("Error occurred");
        }
        return "Hello from Quarkus REST with op=" + op;
    }
}
