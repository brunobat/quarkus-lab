package org.acme;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/manual")
    public String helloManualSpan() {
        Span span = tracer.spanBuilder("HelloResource.helloManualSpan").startSpan();
        try (Scope scope = span.makeCurrent()) {
            return "Hello from Quarkus REST";
        } catch (Exception ignored) {
            span.recordException(ignored);
        } finally {
            span.end();
        }
        return "fail";
    }

}
