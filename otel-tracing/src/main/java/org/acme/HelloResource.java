package org.acme;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

@Path("/hello")
public class HelloResource {

    private static final Logger LOG = Logger.getLogger(HelloResource.class);
    private final Tracer tracer;

    public HelloResource(Tracer tracer,   ManagedExecutor executor) {
        this.tracer = tracer;

    }


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

//        Span span = tracer.spanBuilder("mySpan").startSpan();
//        try (Scope scope = span.makeCurrent()) {
//
//            executor.supplyAsync(() -> {
//                LOG.info("hello-tracing");
//                return "Hello from Quarkus REST";
//            }).thenAccept(result -> {
//                LOG.info("hello-tracing");
//            });
//
//            LOG.info("hello-tracing");
//        } catch (Throwable t) {
//            span.recordException(t);
//        } finally {
//            span.end();
//        }

        LOG.info("hello-tracing");
        return "Hello from Quarkus REST";
    }
}
