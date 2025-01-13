package org.acme;

import io.opentelemetry.api.trace.Tracer;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@Path("/hello")
public class HelloResource {

    private static final Logger LOG = Logger.getLogger(HelloResource.class);
    private final Tracer tracer;

    public HelloResource(Tracer tracer) {
        this.tracer = tracer;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        LOG.info("hello-tracing");
        return "Hello from Quarkus REST";
    }
}
