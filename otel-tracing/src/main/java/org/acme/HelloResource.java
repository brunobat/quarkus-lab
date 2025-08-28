package org.acme;

import io.opentelemetry.api.trace.Tracer;
import io.quarkus.logging.Log;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.context.ManagedExecutor;

@Path("/hello")
public class HelloResource {

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

        Log.info("hello-tracing");
        return "Hello from Quarkus REST";
    }
}
